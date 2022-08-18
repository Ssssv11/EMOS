package com.hjc.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface DeptService {
    ArrayList<HashMap> searchAll();

    void updateDeptById(HashMap param);
    void insertDept(String deptName);

    void deleteDept(int deptId);
}
