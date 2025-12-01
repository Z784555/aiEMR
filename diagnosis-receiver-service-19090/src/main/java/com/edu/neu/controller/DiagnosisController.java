package com.edu.neu.controller;

import com.edu.neu.entity.Diagnosis;
import com.edu.neu.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagnosis")
public class DiagnosisController {
    @Autowired
    DiagnosisService service;

    @PostMapping("/receive")
    public Integer receiveDiagnosis(@RequestBody String content){
        System.out.println("接收到的原始JSON字符串: " + content);
        int id = service.addDiagnosis(new Diagnosis(null, content));
        if(id > 0) {
            return id;
        } else {
            System.out.println("诊断导入失败");
            return -1;
        }
    }

    @GetMapping("/find")
    public  String find(@RequestParam Integer id){
        return service.findById(id);
    }
}
