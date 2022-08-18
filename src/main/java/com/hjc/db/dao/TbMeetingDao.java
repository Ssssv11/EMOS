package com.hjc.db.dao;

import com.hjc.db.pojo.TbMeeting;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface TbMeetingDao {
    int insertMeeting(TbMeeting entity);

    ArrayList<HashMap> searchMyMeetingListByPage(HashMap param);

    boolean searchMeetingMembersInSameDept(String uuid);

    int updateMeetingInstanceId(HashMap map);

    HashMap searchMeetingById(int id);

    ArrayList<HashMap> searchMeetingMembers(int id);

    int updateMeetingInfo(HashMap param);

    int deleteMeetingById(int id);

    List<String> searchUserMeetingInMonth(HashMap param);
}