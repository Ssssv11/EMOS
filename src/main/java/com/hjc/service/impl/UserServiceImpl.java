package com.hjc.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hjc.db.dao.TbDeptDao;
import com.hjc.db.dao.TbUserDao;
import com.hjc.db.pojo.MessageEntity;
import com.hjc.db.pojo.TbUser;
import com.hjc.exception.EmosException;
import com.hjc.service.UserService;
import com.hjc.task.MessageTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService {
    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Autowired
    private TbUserDao userDao;

    @Autowired
    private MessageTask messageTask;

    @Autowired
    private TbDeptDao deptDao;

    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String resp = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(resp);
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0) {
            throw new RuntimeException("临时登录凭证错误");
        }
        return openId;
    }

    @Override
    public int registerUser(String registerCode, String code, String nickname, String photo) {
        boolean havaRootUser = userDao.haveRootUser();
        String openId = getOpenId(code);
        HashMap param = new HashMap();
        param.put("openId", openId);
        param.put("nickname", nickname);
        param.put("photo", photo);
        if (registerCode.equals("000000")) {
            if (!havaRootUser) {
                param.put("role", "[0]");
            }
            else {
                throw new EmosException("无法绑定超级管理员账号");
            }
        }
        switch (registerCode) {
            case "111111":
                param.put("role", "[1]");
                break;
            case "222222":
                param.put("role", "[2]");
                break;
            case "333333":
                param.put("role", "[3]");
                break;
        }
        param.put("status", 1);
        param.put("createTime", new Date());
        param.put("root", true);
        userDao.insert(param);
        int id = userDao.searchIdByOpenId(openId);

        MessageEntity entity = new MessageEntity();
        entity.setSenderId(0);
        entity.setSenderName("系统消息");
        entity.setUuid(IdUtil.simpleUUID());
        if (registerCode.equals("000000")) {
            entity.setMsg("欢迎您注册成为超级管理员，请及时更新你的员工个人信息。");
        }
        switch (registerCode) {
            case "111111":
                entity.setMsg("欢迎您注册成为总经理，请及时更新你的员工个人信息。");
                break;
            case "222222":
                entity.setMsg("欢迎您注册成为部门经理，请及时更新你的员工个人信息。");
                break;
            case "333333":
                entity.setMsg("欢迎您注册成为普通员工，请及时更新你的员工个人信息。");
                break;
        }
        entity.setSendTime(new Date());
        messageTask.sendAsync(id + "", entity);
        return id;
    }

    @Override
    public Set<String> searchUserPermissions(int userId) {
        return userDao.searchUserPermissions(userId);
    }

    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        Integer id = userDao.searchIdByOpenId(openId);
        if (id == null) {
            throw new EmosException("账户不存在");
        }
//        messageTask.receiveAsync(id + "");
        return id;
    }

    @Override
    public TbUser searchById(int userId) {
        return userDao.searchById(userId);
    }

    @Override
    public String searchUserHiredate(int userId) {
        return userDao.searchUserHiredate(userId);
    }

    @Override
    public HashMap searchUserSummary(int userId) {
        return userDao.searchUserSummary(userId);
    }

    @Override
    public ArrayList<HashMap> searchUserGroupByDept(String keyword) {
        ArrayList<HashMap> list_1 = deptDao.searchDeptMembers(keyword);
        ArrayList<HashMap> list_2 = userDao.searchUserGroupByDept(keyword);
        for (HashMap map_1 : list_1) {
            long deptId = (Long) map_1.get("id");
            ArrayList members = new ArrayList();
            for (HashMap map_2 : list_2) {
                long id = (Long) map_2.get("deptId");
                if (deptId == id) {
                    members.add(map_2);
                }
            }
            map_1.put("members", members);
        }
        return list_1;
    }

    @Override
    public ArrayList<HashMap> searchMembers(List param) {
        return userDao.searchMembers(param);
    }

    @Override
    public void update(HashMap param) {
        int row =  userDao.update(param);
        if (row != 1) {
            throw new EmosException("用户信息更新失败");
        }
    }

    @Override
    public HashMap searchUserInfo(int userId) {
        return userDao.searchUserInfo(userId);
    }

    @Override
    public HashMap searchUserInfoById(int userId) {
        return userDao.searchUserInfoById(userId);
    }

    @Override
    public void updateMember(HashMap param) {
        int row =  userDao.updateMember(param);
        if (row != 1) {
            throw new EmosException("用户信息更新失败");
        }
    }

    @Override
    public void delMember(int userId) {
        int row =  userDao.delMember(userId);
        if (row != 1) {
            throw new EmosException("用户删除失败");
        }
    }

    @Override
    public void insertMember(HashMap param) {
        int row =  userDao.insertMember(param);
        if (row != 1) {
            throw new EmosException("添加失败");
        }
    }
}
