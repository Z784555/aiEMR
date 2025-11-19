package com.edu.neu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 电子病历实体类
 */
@TableName("emr")
public class EMR {
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
        @TableField ("create_time")
        private LocalDateTime createTime;
        /**
         记录更新时间
         */
        @TableField ("update_time")
        private LocalDateTime updateTime;
}
