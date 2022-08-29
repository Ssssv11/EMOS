package com.hjc.service.impl;

import com.hjc.db.dao.TbRoleDao;
import com.hjc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private TbRoleDao roleDao;

    @Override
    public ArrayList<HashMap> selectRole() {
        return roleDao.selectRole();
    }

    @Override
    public void addRole(HashMap param) {
        roleDao.addRole(param);
    }
}
