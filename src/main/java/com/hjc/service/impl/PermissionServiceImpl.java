package com.hjc.service.impl;

import com.hjc.db.dao.TbPermissionDao;
import com.hjc.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private TbPermissionDao permissionDao;

    @Override
    public ArrayList<HashMap> selectAll() {
        return permissionDao.selectAll();
    }
}
