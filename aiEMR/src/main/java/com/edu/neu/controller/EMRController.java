package com.edu.neu.controller;

import com.edu.neu.tc.DateTimeTools;
import com.edu.neu.tc.EMRTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Component;

import static com.edu.neu.cfg.AIConfig.SYSTEM_PROMPT;

@RestController
@RequestMapping("/emr")
@CrossOrigin
public class EMRController {
    private final ChatClient chatClient;
    public EMRController(OpenAiChatModel  openAiChatModel,
                         ChatMemory chatMemory,
                         DateTimeTools dateTimeTools,
                         EMRTools emrTools) {
        this.chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(dateTimeTools)
                .defaultTools(emrTools)
                .build();
    }

    /**
     * AI系统依据医生意图或预设诊疗规则，智能解析并执行个性化诊疗流程。
     * 作者：
     * 状态：未完成
     */
    @GetMapping("/flow")
    public String chatFlow() {
        return null;
    }

    /**
     * AI大模型综合患者健康数据与处方内容，自动生成符合医学规范的诊疗记录。
     * 作者：
     * 状态：未完成
     */
    @GetMapping("/record")
    public String chatRecord() {
        return null;
    }

    /**
     * 系统通过智能语义理解与结构化信息抽取，实现电子病历的自动化生成
     * 与使用Tool Calling技术完成归档。
     * 作者：
     * 状态：未完成
     */
    @GetMapping("/gne")
    public String chatGne() {
        return null;
    }
}
