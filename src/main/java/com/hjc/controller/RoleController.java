package com.hjc.controller;

import cn.hutool.json.JSONUtil;
import com.hjc.controller.form.AddRoleForm;
import com.hjc.service.RoleService;
import com.hjc.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/addRole")
    @ApiOperation("添加权限角色")
    public R addRole(@Valid @RequestBody AddRoleForm form) {
        HashMap param = new HashMap();
        param.put("roleName", form.getRoleName());
        param.put("permissions", JSONUtil.toJsonStr(form.getPermissions()).replaceAll("\"", ""));
        System.out.println(param);
        roleService.addRole(param);
        return R.ok().put("result", "success");
    }
}
