package com.hjc.service;

import com.hjc.db.pojo.TbUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface UserService {
    int registerUser(String registerCode, String code, String nickname, String photo);

    Set<String> searchUserPermissions(int userId);

    Integer login(String code);

    TbUser searchById(int userId);

    String searchUserHiredate(int userId);

    HashMap searchUserSummary(int userId);

    ArrayList<HashMap> searchUserGroupByDept(String keyword);

    ArrayList<HashMap> searchMembers(List param);

    void update(HashMap param);

    HashMap searchUserInfo(int userId);

    HashMap searchUserInfoById(int userId);

    void updateMember(HashMap param);

    void delMember(int userId);

    void insertMember(HashMap param);

    ArrayList<HashMap> searchUserRoleGroupByDept(String keyword);

    void updateUserRole(HashMap param);

    String selectUserPhoto(int userId);

    String selectUserName(int userId);
}
