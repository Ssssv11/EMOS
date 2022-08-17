package com.hjc.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
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

import java.util.ArrayList;
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
            }
//            else if (now.isAfter(attendanceEnd)) {
//                return "超出上班考勤结束时间";
//            }
            else {
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
//        else {
//            throw new EmosException("超出考勤时间段，无法考勤");
//        }
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

    @Override
    public HashMap searchTodayCheckin(int userId) {
        HashMap map = checkinDao.searchTodayCheckin(userId);
        return map;
    }

    @Override
    public long searchCheckinDays(int userId) {
        long days = checkinDao.searchCheckinDays(userId);
        return days;
    }

    @Override
    public ArrayList<HashMap> searchWeekCheckin(HashMap param) {
        ArrayList<HashMap> checkinList = checkinDao.searchWeekCheckin(param);
        ArrayList holidaysList = holidaysDao.searchHolidaysInRange(param);
        ArrayList workdayList = workdayDao.searchWorkdayInRange(param);
        DateTime startDate = DateUtil.parseDate(param.get("startDate").toString());
        DateTime endDate = DateUtil.parseDate(param.get("endDate").toString());
        DateRange range = DateUtil.range(startDate, endDate, DateField.DAY_OF_MONTH);
        ArrayList<HashMap> list = new ArrayList<>();
        range.forEach(one -> {
            String date = one.toString("yyyy-MM-dd");
            String type = "工作日";
            if (one.isWeekend()) {
                type = "节假日";
            }
            if (holidaysList != null && holidaysList.contains(date)) {
                type = "节假日";
            } else if (workdayList != null && workdayList.contains(date)) {
                type = "工作日";
            }
            String status = "";
            if (type.equals("工作日") && DateUtil.compare(one, DateUtil.date()) <= 0) {
                status = "缺勤";
                boolean flag = false;
                for (HashMap<String, String> map : checkinList) {
                    if (map.containsValue(date)) {
                        status = map.get("status");
                        flag = true;
                        break;
                    }
                }
                DateTime endTime = DateUtil.parse(DateUtil.today() + " " + constants.attendanceEndTime);
                String today = DateUtil.today();
                if (date.equals(today) && DateUtil.date().isBefore(endTime) && flag == false) {
                    status = "";
                }
            }
            HashMap map = new HashMap();
            map.put("date", date);
            map.put("status", status);
            map.put("type", type);
            map.put("day", one.dayOfWeekEnum().toChinese("周"));
            list.add(map);
        });
        return list;
    }
}


