package com.neusoft.neu23.service;

import com.neusoft.neu23.entity.Patient;

public interface PatientService {
    /**
     * 添加患者信息
     */
    Integer addPatient(Patient patient);
    
    /**
     * 根据患者ID查询患者信息
     */
    Patient getPatientById(Integer patientId);
}


