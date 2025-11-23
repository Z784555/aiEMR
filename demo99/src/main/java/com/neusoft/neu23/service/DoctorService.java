package com.neusoft.neu23.service;

import com.neusoft.neu23.entity.Doctor;

import java.util.List;

public interface DoctorService {
    /**
     * 根据科室ID和日期查询可用的医生列表
     */
    List<Doctor> findAvailableDoctors(Integer deptId, String date);
    
    /**
     * 根据科室ID查询所有医生
     */
    List<Doctor> findDoctorsByDeptId(Integer deptId);
    
    /**
     * 根据医生ID查询医生信息
     */
    Doctor getDoctorById(Integer doctorId);
}


