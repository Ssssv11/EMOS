package com.hjc.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbRoleDao {
    ArrayList<HashMap> selectRole();
}