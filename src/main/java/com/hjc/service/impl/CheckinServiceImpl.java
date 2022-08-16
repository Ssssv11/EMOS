package com.hjc.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hjc.db.dao.TbCheckinDao;
import com.hjc.db.dao.TbCityDao;
import com.hjc.db.dao.TbHolidaysDao;
import com.hjc.db.dao.TbWorkdayDao;
import com.hjc.db.pojo.TbCheckin;
import com.hjc.dto.SystemConstants;
import com.hjc.exception.EmosException;
import com.hjc.service.CheckinService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Autowired
    private TbCityDao tbCityDao;
    @Value("${emos.address}")
    private String target;

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

    @Override
    public void checkin(HashMap param) {
        Date d1 = DateUtil.date();
        Date d2 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceTime);
        Date d3 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceEndTime);
        int status = 1;
        if (d1.compareTo(d2) <= 0) {
            status = 1;
        } else if (d1.compareTo(d2) > 0 && d1.compareTo(d3) < 0) {
            status = 2;
        }
        else {
            throw new EmosException("超出考勤时间段，无法考勤");
        }
        int userId = (Integer) param.get("userId");

        //查询疫情风险等级
        int risk = 1;
        String city = (String) param.get("city");
        String district = (String) param.get("district");
        String address = (String) param.get("address");
        String country = (String) param.get("country");
        String province = (String) param.get("province");
        log.error(address);
        if (StrUtil.isBlank(address) || !address.equals(target)) {
            throw new EmosException("位置不符");
        } else {
            if (!StrUtil.isBlank(city) && !StrUtil.isBlank(district)) {
                String code = tbCityDao.searchCode(city);
                try {
                    String url = "http://m." + code + ".bendibao.com/news/yqdengji/?qu=" + district;
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.getElementsByClass("list-content");
                    if (elements.size() > 0) {
                        Element element = elements.get(0);
                        String result = element.select("p:last-child").text();
                        if ("高风险".equals(result)) {
                            risk = 3;
                        } else if ("中风险".equals(result)) {
                            risk = 2;
                        }
                    }
                } catch (Exception e) {
                    log.error("执行异常", e);
                    throw new EmosException("获取风险等级失败");
                }
            }
        }
        TbCheckin entity = new TbCheckin();
        entity.setUserId(userId);
        entity.setAddress(address);
        entity.setCountry(country);
        entity.setProvince(province);
        entity.setCity(city);
        entity.setDistrict(district);
        entity.setStatus((byte) status);
        entity.setRisk(risk);
        entity.setDate(DateUtil.today());
        entity.setCreateTime(d1);
        checkinDao.insert(entity);
    }
}


