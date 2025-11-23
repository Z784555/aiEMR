package com.neusoft.neu23.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.neu23.entity.Doctor;
import com.neusoft.neu23.mapper.DoctorMapper;
import com.neusoft.neu23.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {
    @Override
    public List<Doctor> findAvailableDoctors(Integer deptId, String date) {
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getDeptId, deptId)
               .eq(Doctor::getIsAvailable, 1)  // 在诊
               .eq(Doctor::getScheduleDate, date);  // 指定日期
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Doctor> findDoctorsByDeptId(Integer deptId) {
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getDeptId, deptId)
               .eq(Doctor::getIsAvailable, 1);  // 只查询在诊的医生
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Doctor getDoctorById(Integer doctorId) {
        return baseMapper.selectById(doctorId);
    }
}


