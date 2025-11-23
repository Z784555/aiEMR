package com.neusoft.neu23.service;

import com.neusoft.neu23.entity.Registration;

public interface RegistrationService {
    /**
     * 创建挂号记录
     */
    Integer createRegistration(Registration registration);
    
    /**
     * 根据挂号ID查询挂号信息
     */
    Registration getRegistrationById(Integer registrationId);
}


