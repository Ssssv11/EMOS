package com.hjc.controller;

import cn.hutool.core.util.IdUtil;
import com.hjc.controller.form.*;
import com.hjc.db.pojo.MessageEntity;
import com.hjc.db.pojo.MessageRefEntity;
import com.hjc.service.MessageService;
import com.hjc.service.UserService;
import com.hjc.task.MessageTask;
import com.hjc.utils.JwtUtil;
import com.hjc.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/message")
@Api("消息模块网络接口")
public class MessageController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageTask messageTask;

    @Autowired
    private UserService userService;

    @PostMapping("/searchMessageByPage")
    @ApiOperation("获取分页消息列表")
    public R searchMessageByPage(@Valid @RequestBody SearchMessageByPageForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        int page = form.getPage();
        int length = form.getLength();
        long start = (page - 1) * length;
        List<HashMap> list = messageService.searchMessageByPage(userId, start, length);
        return R.ok().put("result", list);
    }

    @PostMapping("/searchMessageById")
    @ApiOperation("根据ID查询消息")
    public R searchMessageById(@Valid @RequestBody SearchMessageByIdForm form) {
        HashMap map = messageService.searchMessageById(form.getId());
        return R.ok().put("result", map);
    }

    @PostMapping("/updateUnreadMessage")
    @ApiOperation("未读消息更新成已读消息")
    public R updateUnreadMessage(@Valid @RequestBody UpdateUnreadMessageForm form) {
        long rows = messageService.updateUnreadMessage(form.getId());
        return R.ok().put("result", rows == 1 ? true : false);
    }

    @PostMapping("/deleteMessageRefById")
    @ApiOperation("删除消息")
    public R deleteMessageRefById(@Valid @RequestBody DeleteMessageRefByIdForm form) {
        long rows = messageService.deleteMessageRefById(form.getId());
        return R.ok().put("result", rows == 1 ? true : false);
    }

    @GetMapping("/refreshMessage")
    @ApiOperation("刷新用户消息")
    public R refreshMessage(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        messageTask.receiveAsync(userId + "");
        long lastRows = messageService.searchLastCount(userId);
        long unreadRows = messageService.searchUnreadCount(userId);
        return R.ok().put("lastRows", lastRows).put("unreadRows", unreadRows);
    }

    @PostMapping("/sendMessage")
    @ApiOperation("发送消息")
    public R sendMessage(@Valid @RequestBody SendMessageForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        for (int member : form.getMembers()) {
            MessageEntity message = new MessageEntity();
            message.setUuid(IdUtil.simpleUUID());
            message.setSenderId(userId);
            message.setSenderName(userService.selectUserName(userId));
            message.setMsg(form.getText());
            message.setSendTime(new Date());
            message.setSenderPhoto(userService.selectUserPhoto(userId));
            String id = messageService.insertMessage(message);

            MessageRefEntity ref = new MessageRefEntity();
            ref.setMessageId(id);
            ref.setReadFlag(false);
            ref.setLastFlag(true);
            ref.setReceiverId(member);
            messageService.insertRef(ref);
        }
        return R.ok().put("result", "success");
    }
}
