package com.neusoft.neu23.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.neu23.entity.Department;
import com.neusoft.neu23.mapper.DepartmentMapper;
import com.neusoft.neu23.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Override
    public List<Department> findDepartmentsBySymptoms(String symptoms) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        // 根据症状关键词模糊匹配科室的主治症状
        wrapper.like(Department::getMainSymptoms, symptoms);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Department getDepartmentByName(String deptName) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getDeptName, deptName);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<Department> getAllDepartments() {
        return baseMapper.selectList(null);
    }
}


