package com.hjc.service.impl;

import com.hjc.db.dao.TbDeptDao;
import com.hjc.exception.EmosException;
import com.hjc.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class DeptServiceImpl implements DeptService {
    @Autowired
    private TbDeptDao deptDao;

    @Override
    public ArrayList<HashMap> searchAll() {
        return deptDao.searchAll();
    }

    @Override
    public void updateDeptById(HashMap param) {
        int row = deptDao.updateDeptById(param);
        if (row != 1) {
            throw new EmosException("修改失败");
        }
    }

    @Override
    public void insertDept(String deptName) {
        int row = deptDao.insertDept(deptName);
        if (row != 1) {
            throw new EmosException("添加失败");
        }
    }

    @Override
    public void deleteDept(int deptId) {
        int row = deptDao.deleteDept(deptId);
        if (row != 1) {
            throw new EmosException("删除失败");
        }
    }
}
