package com.neusoft.neu23.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 科室实体类
 */
@TableName("department")
public class Department {
    /**
     * 科室编号（主键）
     */
    @TableId(type = IdType.AUTO, value = "dept_id")
    private Integer deptId;
    
    /**
     * 科室名称
     */
    @TableField("dept_name")
    private String deptName;
    
    /**
     * 科室描述
     */
    @TableField("description")
    private String description;
    
    /**
     * 科室主治症状（用于AI匹配）
     */
    @TableField("main_symptoms")
    private String mainSymptoms;

    public Department() {
    }

    public Department(Integer deptId, String deptName, String description, String mainSymptoms) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.description = description;
        this.mainSymptoms = mainSymptoms;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainSymptoms() {
        return mainSymptoms;
    }

    public void setMainSymptoms(String mainSymptoms) {
        this.mainSymptoms = mainSymptoms;
    }
}


