package com.neusoft.neu23.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 挂号记录实体类
 */
@TableName("registration")
public class Registration {
    /**
     * 挂号编号（主键）
     */
    @TableId(type = IdType.AUTO, value = "registration_id")
    private Integer registrationId;
    
    /**
     * 患者编号
     */
    @TableField("patient_id")
    private Integer patientId;
    
    /**
     * 医生编号
     */
    @TableField("doctor_id")
    private Integer doctorId;
    
    /**
     * 科室编号
     */
    @TableField("dept_id")
    private Integer deptId;
    
    /**
     * 挂号日期
     */
    @TableField("appointment_date")
    private String appointmentDate;
    
    /**
     * 挂号时间段
     */
    @TableField("appointment_time")
    private String appointmentTime;
    
    /**
     * 挂号状态（0-已取消，1-已预约，2-已完成）
     */
    @TableField("status")
    private Integer status;

    public Registration() {
    }

    public Registration(Integer registrationId, Integer patientId, Integer doctorId, Integer deptId, 
                       String appointmentDate, String appointmentTime, Integer status) {
        this.registrationId = registrationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.deptId = deptId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}


