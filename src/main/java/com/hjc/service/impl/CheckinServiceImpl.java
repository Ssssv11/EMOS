package com.hjc.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.hjc.db.dao.TbCheckinDao;
import com.hjc.db.dao.TbHolidaysDao;
import com.hjc.db.dao.TbWorkdayDao;
import com.hjc.dto.SystemConstants;
import com.hjc.service.CheckinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Scope("prototype")
@Slf4j
public class CheckinServiceImpl implements CheckinService {
    @Autowired
    private SystemConstants constants;
    @Autowired
    private TbHolidaysDao holidaysDao;
    @Autowired
    private TbWorkdayDao workdayDao;
    @Autowired
    private TbCheckinDao checkinDao;

    @Override
    public String validCanCheckIn(int userId, String date) {
        boolean b1 = holidaysDao.searchTodayIsHolidays() != null;
        boolean b2 = workdayDao.searchTodayIsWorkday() != null;
        String type = "工作日";
        if (DateUtil.date().isWeekend()) {
            type = "节假日";
        }
        if (b1) {
            type = "节假日";
        } else if (b2) {
            type = "工作日";
        }

        if (type.equals("节假日")) {
            return "节假日不需要考勤";
        } else {
            DateTime now = DateUtil.date();
            String start = DateUtil.today() + " " + constants.attendanceStartTime;
            String end = DateUtil.today() + " " + constants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(start);
            DateTime attendanceEnd = DateUtil.parse(end);
            if (now.isBefore(attendanceStart)) {
                return "未到上班考勤开始时间";
            } else if (now.isAfter(attendanceEnd)) {
                return "超出上班考勤结束时间";
            } else {
                HashMap map = new HashMap();
                map.put("userId", userId);
                map.put("date", date);
                map.put("start", start);
                map.put("end", end);
                boolean bool = checkinDao.haveCheckin(map) != null;
                return bool ? "今日已考勤" : "可以考勤";
            }
        }
    }
}
