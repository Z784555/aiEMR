package com.edu.neu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 电子病历实体类
 */
@Data
@TableName("emr")
public class EMR {
    /**
     * 大模型回复
     */
    private String reply;

    @TableId(type = IdType.AUTO, value = "id")
    private Long id;
    /**
     患者姓名
     */
    @TableField("patient_name")
    private String patientName;
    /**
     性别（男 / 女 / 其他）
     */
    @TableField ("gender")
    private String gender;
    /**
     年龄
     */
    @TableField ("age")
    private Integer age;
    /**
     联系电话
     */
    @TableField ("phone")
    private String phone;
    /**
     家庭住址
     */
    @TableField ("address")
    private String address;
    /**
     就诊号（唯一）
     */
    @TableField ("visit_no")
    private String visitNo;
    /**
     就诊科室
     */
    @TableField ("dept_name")
    private String deptName;
    /**
     就诊时间
     */
    @TableField ("visit_time")
    private LocalDateTime visitTime;
    /**
     就诊类型（初诊 / 复诊）
     */
    @TableField ("visit_type")
    private String visitType;
    /**
     主诉
     */
    @TableField ("main_complaint")
    private String mainComplaint;
    /**
     现病史
     */
    @TableField ("present_illness")
    private String presentIllness;
    /**
     既往史
     */
    @TableField ("past_illness")
    private String pastIllness;
    /**
     过敏史
     */
    @TableField ("allergy_history")
    private String allergyHistory;
    /**
     诊断结果
     */
    @TableField ("diagnosis")
    private String diagnosis;
    /**
     诊断处方
     */
    @TableField ("prescription")
    private String prescription;
    /**
     医嘱建议
     */
    @TableField ("suggestion")
    private String suggestion;
    /**
     医师姓名
     */
    @TableField ("doctor_name")
    private String doctorName;
    /**
     医师手签图片 URL
     */
    @TableField ("signature_url")
    private String signatureUrl;
    /**
     记录创建时间
     */
    @TableField (value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     记录更新时间
     */
    @TableField (value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public EMR(Long id, String patientName, String gender, Integer age, String phone, String address, String visitNo, String deptName, LocalDateTime visitTime, String visitType, String mainComplaint, String presentIllness, String pastIllness, String allergyHistory, String diagnosis, String prescription, String suggestion, String doctorName, String signatureUrl, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.patientName = patientName;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.visitNo = visitNo;
        this.deptName = deptName;
        this.visitTime = visitTime;
        this.visitType = visitType;
        this.mainComplaint = mainComplaint;
        this.presentIllness = presentIllness;
        this.pastIllness = pastIllness;
        this.allergyHistory = allergyHistory;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.suggestion = suggestion;
        this.doctorName = doctorName;
        this.signatureUrl = signatureUrl;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
