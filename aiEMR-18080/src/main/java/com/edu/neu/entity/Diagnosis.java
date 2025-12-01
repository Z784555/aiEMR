package com.edu.neu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("diagnosis")
public class Diagnosis {
    @TableId(type = IdType.AUTO, value = "id")
    private Integer id;
    /**
     * 诊断内容
     */
    @TableField("content")
    private String content;

    public Diagnosis(Integer id, String content) {
        this.id = id;
        this.content = content;
    }
}
