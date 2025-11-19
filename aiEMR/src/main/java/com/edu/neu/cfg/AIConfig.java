package com.edu.neu.cfg;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    private final ChatMemory chatMemory;

    public AIConfig(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    /**
     * 预设系统角色：病历助手
     */
    public static final String SYSTEM_PROMPT = """
            你是医疗AI助手，需根据医生需求解析诊疗流程、生成病历。
            当需要获取当前时间、查询患者信息、调用归档工具时，自动使用提供的工具；
            输出需符合医学规范，优先调用工具而非直接回答。
            """;

    /**
     *
     */
    public static final String FLOW_PROMPT = """
            
            """;

    public static final String RECORD_PROMPT = """
            
            """;

    public static final String GNE_PROMPT = """
            
            """;
}
