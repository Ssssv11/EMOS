package com.hjc;

import com.hjc.db.dao.TbDeptDao;
import com.hjc.service.MeetingService;
import com.hjc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmosWxApiApplicationTests {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private TbDeptDao deptDao;
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
//        for (int i = 1; i <= 100; i++) {
//            TbMeeting meeting = new TbMeeting();
//            meeting.setId((long)i);
//            meeting.setUuid(IdUtil.simpleUUID());
//            meeting.setTitle("测试会议 " + i);
//            meeting.setCreatorId(18L);
//            meeting.setDate(DateUtil.today());
//            meeting.setPlace("线上会议室");
//            meeting.setStart("08:30");
//            meeting.setEnd("10:30");
//            meeting.setType((short)1);
//            meeting.setMembers("[18, 19, 20]");
//            meeting.setDesc("讨论Emos项目上线测试");
//            meeting.setInstanceId(IdUtil.simpleUUID());
//            meeting.setStatus((short)3);
//            meeting.setCreateTime(new Date());
//            meetingService.insertMeeting(meeting);
//        }
        System.out.println(userService.searchUserInfoById(18));
    }

}
