package com.neusoft.neu23.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.neu23.entity.Patient;
import com.neusoft.neu23.mapper.PatientMapper;
import com.neusoft.neu23.service.PatientService;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
    @Override
    public Integer addPatient(Patient patient) {
        patient.setPatientId(null);  // 设置id为null，可以启动数据库的自动递增列功能
        int i = baseMapper.insert(patient);
        if (i > 0) {
            return patient.getPatientId(); // 返回插入自动递增列的id值
        }
        return -1;
    }

    @Override
    public Patient getPatientById(Integer patientId) {
        return baseMapper.selectById(patientId);
    }
}


