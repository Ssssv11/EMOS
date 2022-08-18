package com.hjc.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbDeptDao {
    ArrayList<HashMap> searchDeptMembers(String keyword);
    ArrayList<HashMap> searchAll();

    int updateDeptById(HashMap param);

    int insertDept(String deptName);

    int deleteDept(int deptId);
}