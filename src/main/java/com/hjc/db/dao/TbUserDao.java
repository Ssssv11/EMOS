package com.hjc.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Set;

@Mapper
public interface TbUserDao {
    boolean haveRootUser();
    int insert(HashMap param);
    Integer searchIdByOpenId(String openId);

    Set<String> searchUserPermissions(int userId);
}