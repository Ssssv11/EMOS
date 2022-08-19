package com.hjc.db.dao;

import com.hjc.db.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Mapper
public interface TbUserDao {
    boolean haveRootUser();

    int insert(HashMap param);

    Integer searchIdByOpenId(String openId);

    Set<String> searchUserPermissions(int userId);

    TbUser searchById(int userId);

    String searchUserHiredate(int userId);

    HashMap searchUserSummary(int userId);

    ArrayList<HashMap> searchUserGroupByDept(String keyword);

    ArrayList<HashMap> searchMembers(List param);

    HashMap searchUserInfo(int userId);

    int searchDeptManagerId(int id);

    int searchGmId();

    List<HashMap> selectUserPhotoAndName(List param);

    String searchMemberEmail(int id);

    int update(HashMap param);

    HashMap searchUserInfoById(int userId);

    int updateMember(HashMap param);

    int delMember(int userId);

    int insertMember(HashMap param);

    ArrayList<HashMap> searchUserRoleGroupByDept(String keyword);

    int updateUserRole(HashMap param);

    String selectUserPhoto(int userId);
    String selectUserName(int userId);
}