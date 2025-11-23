package com.neusoft.neu23.service;

import com.neusoft.neu23.entity.Department;

import java.util.List;

public interface DepartmentService {
    /**
     * 根据症状关键词查询匹配的科室
     */
    List<Department> findDepartmentsBySymptoms(String symptoms);
    
    /**
     * 根据科室名称查询科室
     */
    Department getDepartmentByName(String deptName);
    
    /**
     * 查询所有科室
     */
    List<Department> getAllDepartments();
}


