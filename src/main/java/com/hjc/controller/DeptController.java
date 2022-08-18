package com.hjc.controller;

import com.hjc.service.DeptService;
import com.hjc.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/dept")
@Api("部门模块接口")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @GetMapping("/getAllDept")
    @ApiOperation("获取部门")
    public R getAll() {
        ArrayList<HashMap> list = deptService.searchAll();
        return R.ok().put("result", list);
    }
}
