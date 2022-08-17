package com.hjc.db.dao;

import com.hjc.db.pojo.TbCheckin;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbCheckinDao {
    Integer haveCheckin(HashMap param);
    void insert(TbCheckin checkin);
    HashMap searchTodayCheckin(int userId);
    long searchCheckinDays(int userId);
    ArrayList<HashMap> searchWeekCheckin(HashMap param);
}