package com.hjc.controller;

import com.hjc.service.PermissionService;
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
@RequestMapping("/permission")
@Api("权限细分模块")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/selectAll")
    @ApiOperation("/查询所有细分权限")
    public R selectAll() {
        ArrayList<HashMap> res = permissionService.selectAll();
        return R.ok().put("result", res);
    }
}
