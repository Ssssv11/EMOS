package com.hjc.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.hjc.controller.form.CheckinForm;
import com.hjc.controller.form.SearchMonthCheckinForm;
import com.hjc.dto.SystemConstants;
import com.hjc.exception.EmosException;
import com.hjc.service.CheckinService;
import com.hjc.service.UserService;
import com.hjc.utils.JwtUtil;
import com.hjc.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/checkin")
@Api("签到模块接口")
@Slf4j
public class CheckinController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CheckinService checkinService;
    @Autowired
    private UserService userService;
    @Autowired
    private SystemConstants constants;

    @GetMapping("/validCanCheckIn")
    @ApiOperation("查看用户今天是否可以签到")
    public R validCanCheckIn(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        String result = checkinService.validCanCheckIn(userId, DateUtil.today());
        return R.ok(result);
    }

    @PostMapping("/checkin")
    @ApiOperation("签到")
    public R checkin(@RequestBody CheckinForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        HashMap param = new HashMap();
        param.put("userId", userId);
        param.put("city", form.getCity());
        param.put("district", form.getDistrict());
        param.put("address", form.getAddress());
        param.put("country", form.getCountry());
        param.put("province", form.getProvince());
        checkinService.checkin(param);
        return new R();
    }

    @GetMapping("/searchTodayCheckin")
    @ApiOperation("查询用户当日签到数据")
    public R searchTodayCheckin(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        HashMap map = checkinService.searchTodayCheckin(userId);
        map.put("attendanceTime", constants.attendanceTime);
        map.put("closingTime", constants.closingTime);
        long days = checkinService.searchCheckinDays(userId);
        map.put("checkinDays", days);

        DateTime hiredate = DateUtil.parse(userService.searchUserHiredate(userId));
        DateTime startDate = DateUtil.beginOfWeek(DateUtil.date());
        if (startDate.isBefore(hiredate)) {
            startDate = hiredate;
        }
        DateTime endDate = DateUtil.endOfWeek(DateUtil.date());
        HashMap param = new HashMap();
        param.put("startDate", startDate.toString());
        param.put("endDate", endDate.toString());
        param.put("userId", userId);
        ArrayList<HashMap> list = checkinService.searchWeekCheckin(param);
        map.put("weekCheckin", list);
        return R.ok().put("result", map);
    }

    @PostMapping("/searchMonthCheckin")
    @ApiOperation("查询用户某月签到数据")
    public R searchMonthCheckin(@Valid @RequestBody SearchMonthCheckinForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        DateTime hiredate = DateUtil.parse(userService.searchUserHiredate(userId));
        String month = form.getMonth() < 10 ? "0" + form.getMonth() : form.getMonth().toString();
        DateTime startDate = DateUtil.parse(form.getYear() + "-" + month + "-01");
        if (startDate.isBefore(DateUtil.beginOfMonth(hiredate))) {
            throw new EmosException("只能查询考勤之后日期的数据");
        }
        if (startDate.isBefore(hiredate)) {
            startDate = hiredate;
        }
        DateTime endDate = DateUtil.endOfMonth(startDate);
        HashMap param = new HashMap();
        param.put("userId", userId);
        param.put("startDate", startDate.toString());
        param.put("endDate", endDate.toString());
        ArrayList<HashMap> list = checkinService.searchMonthCheckin(param);
        int sum_1 = 0, sum_2 = 0, sum_3 = 0;
        for (HashMap<String, String> one : list) {
            String type = one.get("type");
            String status = one.get("status");
            if ("工作日".equals(type)) {
                if ("正常".equals(status)) {
                    sum_1++;
                } else if ("迟到".equals(status)) {
                    sum_2++;
                } else if ("缺勤".equals(status)) {
                    sum_3++;
                }
            }
        }
        return R.ok().put("list", list).put("sum_1", sum_1).put("sum_2", sum_2).put("sum_3", sum_3);
    }
}
