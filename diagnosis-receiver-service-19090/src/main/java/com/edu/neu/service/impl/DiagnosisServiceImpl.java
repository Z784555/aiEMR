package com.edu.neu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.neu.entity.Diagnosis;
import com.edu.neu.mapper.DiagnosisMapper;
import com.edu.neu.service.DiagnosisService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class DiagnosisServiceImpl extends ServiceImpl<DiagnosisMapper, Diagnosis> implements DiagnosisService {

    @Override
    public Integer addDiagnosis(Diagnosis diagnosis) {
        diagnosis.setId(null);
        int i = baseMapper.insert(diagnosis);
        if (i>0){
            return diagnosis.getId();
        }
        return -1;
    }

    @Override
    public String findById(Integer id) {
        //1.校验 id 合法性
        if (id == null || id <= 0) {
            return ("id 必须是正整数");
        }

        //2.调用 BaseMapper 内置方法 selectById 查询
        Diagnosis diagnosis = baseMapper.selectById(id);

        return diagnosis.getContent();
    }
}
