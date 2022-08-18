package com.hjc.controller;

import com.hjc.controller.form.DeleteDeptByIdForm;
import com.hjc.controller.form.UpdateDeptByIdForm;
import com.hjc.controller.form.InsertDeptByIdForm;
import com.hjc.service.DeptService;
import com.hjc.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/updateDeptById")
    @ApiOperation("修改部门")
    @RequiresPermissions(value = {"ROOT", "DEPT:UPDATE"}, logical = Logical.OR)
    public R updateDeptById(@Valid @RequestBody UpdateDeptByIdForm form) {
        HashMap param = new HashMap<>();
        param.put("deptId", form.getDeptId());
        param.put("deptName", form.getDeptName());
        deptService.updateDeptById(param);
        return R.ok().put("result", "success");
    }

    @PostMapping("/insertDept")
    @ApiOperation("添加部门")
    @RequiresPermissions(value = {"ROOT", "DEPT:INSERT"}, logical = Logical.OR)
    public R insertDept(@Valid @RequestBody InsertDeptByIdForm form) {
        deptService.insertDept(form.getDeptName());
        return R.ok().put("result", "success");
    }

    @PostMapping("/deleteDept")
    @ApiOperation("删除部门")
    @RequiresPermissions(value = {"ROOT", "DEPT:DELETE"}, logical = Logical.OR)
    public R deleteDept(@Valid @RequestBody DeleteDeptByIdForm form) {
        deptService.deleteDept(form.getDeptId());
        return R.ok().put("result", "success");
    }
}
