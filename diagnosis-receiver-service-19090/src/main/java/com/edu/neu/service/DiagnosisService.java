package com.edu.neu.service;

import com.edu.neu.entity.Diagnosis;

public interface DiagnosisService {
    Integer addDiagnosis(Diagnosis diagnosis);
    String findById(Integer id);
}
