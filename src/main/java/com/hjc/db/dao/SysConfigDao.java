package com.hjc.db.dao;

import com.hjc.db.pojo.SysConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysConfigDao {
    List<SysConfig> selectAllParam();
}