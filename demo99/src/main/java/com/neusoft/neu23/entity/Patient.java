package com.neusoft.neu23.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 患者实体类
 */
@TableName("patient")
public class Patient {
    /**
     * 患者编号（主键）
     */
    @TableId(type = IdType.AUTO, value = "patient_id")
    private Integer patientId;
    
    /**
     * 患者姓名
     */
    @TableField("name")
    private String name;
    
    /**
     * 患者手机号
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 患者年龄
     */
    @TableField("age")
    private Integer age;
    
    /**
     * 患者性别（0-女，1-男）
     */
    @TableField("gender")
    private Integer gender;
    
    /**
     * 主要症状描述
     */
    @TableField("symptoms")
    private String symptoms;

    public Patient() {
    }

    public Patient(Integer patientId, String name, String phone, Integer age, Integer gender, String symptoms) {
        this.patientId = patientId;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.symptoms = symptoms;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}


