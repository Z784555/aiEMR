package com.edu.neu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.neu.entity.EMR;
import com.edu.neu.mapper.EMRMapper;
import com.edu.neu.service.EMRService;
import org.springframework.stereotype.Service;

@Service
public class EMRServiceImpl extends ServiceImpl<EMRMapper, EMR> implements EMRService {
    @Override
    public Long addEMR(EMR emr) {
        emr.setId(null);
        int i = baseMapper.insert(emr);
        if(i>0){
            return emr.getId();
        }
        return (long) -1;
    }

    @Override
    public Long updateEMR(EMR emr) {
        int i = baseMapper.updateById(emr);
        if(i>0){
            return emr.getId();
        }
        return (long) -1;
    }
}
