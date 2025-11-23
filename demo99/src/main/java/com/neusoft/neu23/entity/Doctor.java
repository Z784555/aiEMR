package com.neusoft.neu23.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 医生实体类
 */
@TableName("doctor")
public class Doctor {
    /**
     * 医生编号（主键）
     */
    @TableId(type = IdType.AUTO, value = "doctor_id")
    private Integer doctorId;
    
    /**
     * 医生姓名
     */
    @TableField("name")
    private String name;
    
    /**
     * 所属科室编号
     */
    @TableField("dept_id")
    private Integer deptId;
    
    /**
     * 医生职称
     */
    @TableField("title")
    private String title;
    
    /**
     * 擅长领域
     */
    @TableField("specialty")
    private String specialty;
    
    /**
     * 是否在诊（0-不在诊，1-在诊）
     */
    @TableField("is_available")
    private Integer isAvailable;
    
    /**
     * 坐诊日期（格式：YYYY-MM-DD）
     */
    @TableField("schedule_date")
    private String scheduleDate;
    
    /**
     * 坐诊时间段（如：上午、下午、全天）
     */
    @TableField("schedule_time")
    private String scheduleTime;

    public Doctor() {
    }

    public Doctor(Integer doctorId, String name, Integer deptId, String title, String specialty, 
                  Integer isAvailable, String scheduleDate, String scheduleTime) {
        this.doctorId = doctorId;
        this.name = name;
        this.deptId = deptId;
        this.title = title;
        this.specialty = specialty;
        this.isAvailable = isAvailable;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
}


