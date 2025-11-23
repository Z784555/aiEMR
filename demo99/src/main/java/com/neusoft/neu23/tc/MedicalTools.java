package com.neusoft.neu23.tc;

import com.neusoft.neu23.entity.Department;
import com.neusoft.neu23.entity.Doctor;
import com.neusoft.neu23.entity.Patient;
import com.neusoft.neu23.entity.Registration;
import com.neusoft.neu23.service.DepartmentService;
import com.neusoft.neu23.service.DoctorService;
import com.neusoft.neu23.service.PatientService;
import com.neusoft.neu23.service.RegistrationService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 医疗导诊系统工具类
 * 提供查询科室、查询医生、挂号等功能
 */
@Component
public class MedicalTools {
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private RegistrationService registrationService;

    /**
     * 保存患者信息
     * 用于收集患者的基本信息（姓名、手机号、年龄、性别、症状）
     */
    @Tool(description = "保存患者信息到数据库，需要提供患者的姓名、手机号、年龄、性别和主要症状。返回患者编号。")
    public String savePatientInfo(
            @ToolParam(description = "患者姓名") String name,
            @ToolParam(description = "患者手机号") String phone,
            @ToolParam(description = "患者年龄") Integer age,
            @ToolParam(description = "患者性别，0表示女性，1表示男性") Integer gender,
            @ToolParam(description = "患者主要症状描述") String symptoms) {
        Patient patient = new Patient(null, name, phone, age, gender, symptoms);
        Integer patientId = patientService.addPatient(patient);
        if (patientId > 0) {
            return "患者信息保存成功，患者编号：" + patientId;
        }
        return "患者信息保存失败";
    }

    /**
     * 根据症状查询匹配的科室
     * AI可以根据患者描述的症状来推荐合适的科室
     */
    @Tool(description = "根据患者描述的症状关键词查询匹配的科室列表，用于协助患者确定就诊科室。")
    public List<Department> findDepartmentsBySymptoms(
            @ToolParam(description = "患者描述的症状关键词，如：头痛、发烧、咳嗽等") String symptoms) {
        return departmentService.findDepartmentsBySymptoms(symptoms);
    }

    /**
     * 根据科室名称查询科室信息
     */
    @Tool(description = "根据科室名称查询科室详细信息")
    public Department getDepartmentByName(
            @ToolParam(description = "科室名称，如：内科、外科、儿科等") String deptName) {
        return departmentService.getDepartmentByName(deptName);
    }

    /**
     * 查询所有科室列表
     */
    @Tool(description = "查询医院所有科室列表，用于向患者展示可选择的科室")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    /**
     * 根据科室和日期查询可用的医生列表
     * 结合病情和医生坐诊情况列出出诊医生名单
     */
    @Tool(description = "根据科室编号和日期查询该科室在指定日期可用的医生列表，用于列出出诊医生名单。日期格式为YYYY-MM-DD。")
    public List<Doctor> findAvailableDoctors(
            @ToolParam(description = "科室编号") Integer deptId,
            @ToolParam(description = "就诊日期，格式为YYYY-MM-DD，如：2024-01-15") String date) {
        return doctorService.findAvailableDoctors(deptId, date);
    }

    /**
     * 根据科室编号查询该科室的所有医生
     */
    @Tool(description = "根据科室编号查询该科室所有在诊的医生列表")
    public List<Doctor> findDoctorsByDeptId(
            @ToolParam(description = "科室编号") Integer deptId) {
        return doctorService.findDoctorsByDeptId(deptId);
    }

    /**
     * 完成挂号操作
     * 使用ToolCalling技术完成挂号
     */
    @Tool(description = "为患者完成挂号操作，需要提供患者编号、医生编号、科室编号、预约日期和预约时间段。返回挂号编号。")
    public String createRegistration(
            @ToolParam(description = "患者编号") Integer patientId,
            @ToolParam(description = "医生编号") Integer doctorId,
            @ToolParam(description = "科室编号") Integer deptId,
            @ToolParam(description = "预约日期，格式为YYYY-MM-DD") String appointmentDate,
            @ToolParam(description = "预约时间段，如：上午、下午、全天") String appointmentTime) {
        Registration registration = new Registration(null, patientId, doctorId, deptId, 
                                                      appointmentDate, appointmentTime, 1);
        Integer registrationId = registrationService.createRegistration(registration);
        if (registrationId > 0) {
            return "挂号成功！挂号编号：" + registrationId;
        }
        return "挂号失败，请稍后重试";
    }

    /**
     * 根据患者编号查询患者信息
     */
    @Tool(description = "根据患者编号查询患者详细信息")
    public Patient getPatientById(
            @ToolParam(description = "患者编号") Integer patientId) {
        return patientService.getPatientById(patientId);
    }

    /**
     * 根据医生编号查询医生信息
     */
    @Tool(description = "根据医生编号查询医生详细信息")
    public Doctor getDoctorById(
            @ToolParam(description = "医生编号") Integer doctorId) {
        return doctorService.getDoctorById(doctorId);
    }
}


