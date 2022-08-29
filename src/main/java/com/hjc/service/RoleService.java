package com.hjc.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface RoleService {
    ArrayList<HashMap> selectRole();

    void addRole(HashMap param);
}
