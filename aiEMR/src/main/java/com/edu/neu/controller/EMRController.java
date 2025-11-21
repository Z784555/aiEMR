package com.edu.neu.controller;

import com.edu.neu.tc.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.neu.service.PromptTemplateService;
import static com.edu.neu.cfg.AIConfig.FLOW_PROMPT;
import static com.edu.neu.cfg.AIConfig.GNE_PROMPT;
import static com.edu.neu.cfg.AIConfig.RECORD_PROMPT;
import static com.edu.neu.cfg.AIConfig.SYSTEM_PROMPT;

@RestController
@RequestMapping("/emr")
@CrossOrigin
public class EMRController {
    private final ChatClient chatClient;
    private final PromptTemplateService promptTemplateService;
    public EMRController(OpenAiChatModel  openAiChatModel,
                         ChatMemory chatMemory,
                         DateTimeTools dateTimeTools,
                         PromptTemplateService promptTemplateService) {
        this.promptTemplateService = promptTemplateService;
        this.chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(dateTimeTools)
                .build();
    }

    /**
     * AI系统依据医生意图或预设诊疗规则，智能解析并执行个性化诊疗流程。
     * 作者：
     * 状态：未完成
     */
    @GetMapping("/flow")
    public String chatFlow(@RequestParam(value = "prompt", required = false, defaultValue = "") String prompt) {
        return invokeChat(FLOW_PROMPT, prompt);
    }

    /**
     * AI大模型综合患者健康数据与处方内容，自动生成符合医学规范的诊疗记录。
     * 作者：
     * 状态：未完成
     */
    @GetMapping("/record")
    public String chatRecord(@RequestParam(value = "prompt", required = false, defaultValue = "") String prompt) {
        return invokeChat(RECORD_PROMPT, prompt);
    }

    /**
     * 系统通过智能语义理解与结构化信息抽取，实现电子病历的自动化生成
     * 与使用Tool Calling技术完成归档。
     * 作者：
     * 状态：未完成
     */
    @GetMapping("/gne")
    public String chatGne(@RequestParam(value = "prompt", required = false, defaultValue = "") String prompt) {
        return invokeChat(GNE_PROMPT, prompt);
    }

    private String invokeChat(String taskPrompt, String doctorInput) {
        String systemPrompt = SYSTEM_PROMPT + "\n" + taskPrompt;
        String userMessage = promptTemplateService.resolvePrompt(doctorInput);
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .call()
                .content();
    }
}
