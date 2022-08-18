package com.hjc.service;

import com.hjc.db.pojo.TbMeeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface MeetingService {
    void insertMeeting(TbMeeting entity);

    ArrayList<HashMap> searchMyMeetingListByPage(HashMap param);

    HashMap searchMeetingById(int id);

    void updateMeetingInfo(HashMap param);

    void deleteMeetingById(int id);

    Long searchRoomIdByUUID(String uuid);

    List<String> searchUserMeetingInMonth(HashMap param);

}
