package com.hjc.controller;

import com.hjc.service.RoleService;
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
@RequestMapping("/role")
@Api("权限模块接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/selectRole")
    @ApiOperation("查询权限信息")
    public R selectRole() {
        ArrayList<HashMap> map = roleService.selectRole();
        return R.ok().put("result", map);
    }
}
