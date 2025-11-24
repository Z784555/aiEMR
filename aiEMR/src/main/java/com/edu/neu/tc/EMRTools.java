package com.edu.neu.tc;

import com.edu.neu.entity.EMR;
import com.edu.neu.service.EMRService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EMRTools {
    @Autowired
    private EMRService emrService;

    @Tool(description = "保存患者就诊记录及病历信息到数据库，返回唯一就诊记录ID。" +
            "必须收集：患者姓名、性别、年龄、就诊号、就诊科室、就诊时间、就诊类型、主诉、医师姓名；" +
            "可选收集：联系电话、家庭住址、现病史、既往史、过敏史、诊断结果、处方、医嘱建议、医师手签URL")
    public String saveEMR(
            @ToolParam(description = "患者姓名，必填，如：蔡志军") String patientName,
            @ToolParam(description = "患者性别，必填，仅支持：男/女/其他") String gender,
            @ToolParam(description = "患者年龄，必填，范围0-120，如：45") Integer age,
            @ToolParam(description = "就诊号，必填，唯一标识，如：MZ07882405098") String visitNo,
            @ToolParam(description = "就诊科室，必填，如：中医内科") String deptName,
            @ToolParam(description = "就诊时间，必填，格式：yyyy-MM-dd HH:mm:ss，如：2024-05-09 14:21:52") String visitTime,
            @ToolParam(description = "就诊类型，必填，仅支持：初诊/复诊") String visitType,
            @ToolParam(description = "主诉，必填，患者就诊主要原因，如：反复头晕3天") String mainComplaint,
            @ToolParam(description = "医师姓名，必填，开具病历的医师名称，如：贾连荣") String doctorName,
            @ToolParam(description = "联系电话，可选，患者手机号或固定电话，如：13920631008") String phone,
            @ToolParam(description = "家庭住址，可选，患者居住地址，如：上海市宝山区上大路99号") String address,
            @ToolParam(description = "现病史，可选，当前病症的详细描述") String presentIllness,
            @ToolParam(description = "既往史，可选，患者过往病史情况") String pastIllness,
            @ToolParam(description = "过敏史，可选，患者药物/食物过敏情况，无则填：无") String allergyHistory,
            @ToolParam(description = "诊断结果，可选，医师给出的诊断结论") String diagnosis,
            @ToolParam(description = "诊断处方，可选，医师开具的处方内容（含药品、剂量、用法）") String prescription,
            @ToolParam(description = "医嘱建议，可选，医师给出的后续治疗/护理建议") String suggestion,
            @ToolParam(description = "医师手签图片URL，可选，医师电子签名的图片存储地址") String signatureUrl
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Long id = emrService.addEMR(new EMR(
                null,  // id（自增主键，无需传入）
                patientName,
                gender,
                age,
                phone,
                address,
                visitNo,
                deptName,
                LocalDateTime.parse(visitTime,formatter),
                visitType,
                mainComplaint,
                presentIllness,
                pastIllness,
                allergyHistory,
                diagnosis,
                prescription,
                suggestion,
                doctorName,
                signatureUrl,
                null,  // createTime（数据库默认生成，无需传入）
                null   // updateTime（数据库默认更新，无需传入）
        ));
        if (id > 0) {
            // 保存成功后，emr 对象的 id 字段已被 MyBatis-Plus 填充
            return ""+id;
        } else {
            // 处理保存失败的情况，可以抛出异常或返回 null
            throw new RuntimeException("保存 EMR 失败");
        }
    }

    @Tool(description = "更新患者就诊记录及病历信息到数据库，返回唯一就诊记录ID。")
    public String updateEMR(
            @ToolParam(description = "病历id") Long id,
            @ToolParam(description = "患者姓名，必填，如：蔡志军") String patientName,
            @ToolParam(description = "患者性别，必填，仅支持：男/女/其他") String gender,
            @ToolParam(description = "患者年龄，必填，范围0-120，如：45") Integer age,
            @ToolParam(description = "就诊号，必填，唯一标识，如：MZ07882405098") String visitNo,
            @ToolParam(description = "就诊科室，必填，如：中医内科") String deptName,
            @ToolParam(description = "就诊时间，必填，格式：yyyy-MM-dd HH:mm:ss，如：2024-05-09 14:21:52") String visitTime,
            @ToolParam(description = "就诊类型，必填，仅支持：初诊/复诊") String visitType,
            @ToolParam(description = "主诉，必填，患者就诊主要原因，如：反复头晕3天") String mainComplaint,
            @ToolParam(description = "医师姓名，必填，开具病历的医师名称，如：贾连荣") String doctorName,
            @ToolParam(description = "联系电话，可选，患者手机号或固定电话，如：13920631008") String phone,
            @ToolParam(description = "家庭住址，可选，患者居住地址，如：上海市宝山区上大路99号") String address,
            @ToolParam(description = "现病史，可选，当前病症的详细描述") String presentIllness,
            @ToolParam(description = "既往史，可选，患者过往病史情况") String pastIllness,
            @ToolParam(description = "过敏史，可选，患者药物/食物过敏情况，无则填：无") String allergyHistory,
            @ToolParam(description = "诊断结果，可选，医师给出的诊断结论") String diagnosis,
            @ToolParam(description = "诊断处方，可选，医师开具的处方内容（含药品、剂量、用法）") String prescription,
            @ToolParam(description = "医嘱建议，可选，医师给出的后续治疗/护理建议") String suggestion,
            @ToolParam(description = "医师手签图片URL，可选，医师电子签名的图片存储地址") String signatureUrl
    ){
        Long Id = emrService.updateEMR(new EMR(
                id,
                patientName,
                gender,
                age,
                phone,
                address,
                visitNo,
                deptName,
                LocalDateTime.parse(visitTime),
                visitType,
                mainComplaint,
                presentIllness,
                pastIllness,
                allergyHistory,
                diagnosis,
                prescription,
                suggestion,
                doctorName,
                signatureUrl,
                null,  // createTime（数据库默认生成，无需传入）
                null   // updateTime（数据库默认更新，无需传入）
        ));
        if (Id > 0) {
            // 保存成功
            return ""+id;
        } else {
            // 处理保存失败的情况，可以抛出异常或返回 null
            throw new RuntimeException("保存 EMR 失败");
        }
    }
}
