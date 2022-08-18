package com.hjc.controller;

import cn.hutool.json.JSONUtil;
import com.hjc.controller.form.*;
import com.hjc.exception.EmosException;
import com.hjc.service.UserService;
import com.hjc.utils.JwtUtil;
import com.hjc.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api("用户模块接口")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${emos.jwt.cache-expire}")
    private int cacheExpire;

    @PostMapping("/register")
    @ApiOperation("注册用户")
    public R register(@Valid @RequestBody RegisterForm form) {
        int id = userService.registerUser(form.getRegisterCode(), form.getCode(), form.getNickname(), form.getPhoto());
        String token = jwtUtil.createToken(id);
        Set<String> permsSet = userService.searchUserPermissions(id);
        saveCacheToken(token, id);
        return R.ok("注册成功").put("token", token).put("permission", permsSet);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public R login(@Valid @RequestBody LoginForm form) {
        int id = userService.login(form.getCode());
        String token = jwtUtil.createToken(id);
        saveCacheToken(token, id);
        Set<String> permsSet = userService.searchUserPermissions(id);
        return R.ok("登录成功").put("token", token).put("permission", permsSet);
    }

    @GetMapping("/searchUserSummary")
    @ApiOperation("查询用户摘要信息")
    public R searchUserSummary(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        HashMap map = userService.searchUserSummary(userId);
        return R.ok().put("result", map);
    }

    @PostMapping("/searchUserGroupByDept")
    @ApiOperation("查询员工列表，按照部门分组排列")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:SELECT"}, logical = Logical.OR)
    public R searchUserGroupByDept(@Valid @RequestBody SearchUserGroupByDeptForm form) {
        ArrayList<HashMap> list = userService.searchUserGroupByDept(form.getKeyword());
        return R.ok().put("result", list);
    }

    @PostMapping("/searchMembers")
    @ApiOperation("查询成员")
    @RequiresPermissions(value = {"ROOT", "MEETING:INSERT", "MEETING:UPDATE"}, logical = Logical.OR)
    public R searchMembers(@Valid @RequestBody SearchMembersForm form) {
        if (!JSONUtil.isJsonArray(form.getMembers())) {
            throw new EmosException("members不是JSON数组");
        }
        List param = JSONUtil.parseArray(form.getMembers()).toList(Integer.class);
        ArrayList list = userService.searchMembers(param);
        return R.ok().put("result", list);
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation("更新用户信息")
    @RequiresPermissions(value = {"ROOT", "USER:UPDATE"}, logical = Logical.OR)
    public R updateMeetingInfo(@Valid @RequestBody UpdateUserInfoForm form, @RequestHeader("token") String token) {
        if (form.getName() == null  || form.getName().length() == 0) {
            throw new EmosException("姓名不能为空");
        }

        int userId = jwtUtil.getUserId(token);
        HashMap param = new HashMap();
        param.put("name", form.getName());
        param.put("sex", form.getSex());
        param.put("tel", form.getTel());
        param.put("email", form.getEmail());
        param.put("id", userId);
        userService.update(param);
        return R.ok().put("result", "success");
    }

    @PostMapping("/searchUserInfo")
    @ApiOperation("查询用户信息")
    @RequiresPermissions(value = {"ROOT", "USER:SELECT"}, logical = Logical.OR)
    public R searchUserInfo(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        HashMap map = userService.searchUserInfo(userId);
        return R.ok().put("result", map);
    }

    @PostMapping("/searchUserInfoById")
    @ApiOperation("根据ID查询用户信息")
    @RequiresPermissions(value = {"ROOT", "USER:SELECT"}, logical = Logical.OR)
    public R searchMeetingById(@Valid @RequestBody SearchUserInfoForm form) {
        HashMap map = userService.searchUserInfoById(form.getId());
        return R.ok().put("result", map);
    }

    @PostMapping("/updateMemberInfo")
    @ApiOperation("更新用户信息")
    @RequiresPermissions(value = {"ROOT", "DEPT:UPDATE"}, logical = Logical.OR)
    public R updateMemberInfo(@Valid @RequestBody UpdateMemberInfoForm form) {
        if (form.getName() == null  || form.getName().length() == 0) {
            throw new EmosException("姓名不能为空");
        }

        HashMap param = new HashMap();
        param.put("name", form.getName());
        param.put("sex", form.getSex());
        param.put("tel", form.getTel());
        param.put("email", form.getEmail());
        param.put("id", form.getId());
        param.put("deptId", form.getDeptId());
        param.put("hiredate", form.getHiredate());
        userService.updateMember(param);
        return R.ok().put("result", "success");
    }

    @PostMapping("/delMember")
    @ApiOperation("删除用户信息")
    @RequiresPermissions(value = {"ROOT", "DEPT:DELETE"}, logical = Logical.OR)
    public R delMember(@Valid @RequestBody DeleteMemberByIdForm form) {
        userService.delMember(form.getId());
        return R.ok().put("result", "success");
    }

    @PostMapping("/insertMember")
    @ApiOperation("添加用户信息")
    @RequiresPermissions(value = {"ROOT", "DEPT:INSERT"}, logical = Logical.OR)
    public R insertMember(@Valid @RequestBody InsertMemberForm form) {
        HashMap param = new HashMap<>();
        param.put("name", form.getName());
        param.put("sex", form.getSex());
        param.put("tel", form.getTel());
        param.put("email", form.getEmail());
        param.put("hiredate", form.getHiredate());
        param.put("deptId", form.getDeptId());
        param.put("role", "[3]");
        param.put("root", 0);
        param.put("status", 1);
        param.put("createTime", new Date());
        userService.insertMember(param);
        return R.ok().put("result", "success");
    }

    private void saveCacheToken(String token, int userId) {
        redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
    }
}
