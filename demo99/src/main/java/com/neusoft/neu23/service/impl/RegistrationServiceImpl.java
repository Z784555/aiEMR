package com.neusoft.neu23.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.neu23.entity.Registration;
import com.neusoft.neu23.mapper.RegistrationMapper;
import com.neusoft.neu23.service.RegistrationService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements RegistrationService {
    @Override
    public Integer createRegistration(Registration registration) {
        registration.setRegistrationId(null);  // 设置id为null，可以启动数据库的自动递增列功能
        registration.setStatus(1);  // 默认状态为已预约
        int i = baseMapper.insert(registration);
        if (i > 0) {
            return registration.getRegistrationId(); // 返回插入自动递增列的id值
        }
        return -1;
    }

    @Override
    public Registration getRegistrationById(Integer registrationId) {
        return baseMapper.selectById(registrationId);
    }
}


