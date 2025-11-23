package com.edu.neu.controller;

import com.edu.neu.service.PromptTemplateService;
import com.edu.neu.tc.DateTimeTools;
import com.edu.neu.tc.EMRTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

import static com.edu.neu.cfg.AIConfig.*;

@RestController
@RequestMapping("/emr")
@CrossOrigin // 允许跨域（前端调用时有用）
public class EMRController {
    private final ChatClient chatClient;
    private final PromptTemplateService promptTemplateService;

    // 构造函数依赖注入（没问题）
    public EMRController(OpenAiChatModel openAiChatModel,
                         ChatMemory chatMemory,
                         DateTimeTools dateTimeTools,
                         EMRTools emrTools,
                         PromptTemplateService promptTemplateService) {
        this.chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(dateTimeTools)
                .build();
        this.promptTemplateService = promptTemplateService;
    }

    /**
     * AI系统依据医生意图或预设诊疗规则，智能解析并执行个性化诊疗流程。
     */
    @GetMapping("/flow")
    // 关键修改：添加 name = "input"，明确前端参数名
    public String chatFlow(@RequestParam(name = "input", required = false) String input) {
        String userPrompt = promptTemplateService.resolvePrompt(input);
        String fullPrompt = FLOW_PROMPT + "\n\n请基于以下医生输入信息，生成个性化诊疗流程：\n" + userPrompt;

        return chatClient.prompt()
                .user(fullPrompt)
                .call()
                .content();
    }

    /**
     * AI大模型综合患者健康数据与处方内容，自动生成符合医学规范的诊疗记录。
     */
    @GetMapping("/record")
    // 关键修改：添加 name = "input"
    public String chatRecord(@RequestParam(name = "input", required = false) String input) {
        String userPrompt = promptTemplateService.resolvePrompt(input);
        String fullPrompt = RECORD_PROMPT + "\n\n请基于以下医生输入的诊疗意图，生成完整诊疗记录：\n" + userPrompt;

        return chatClient.prompt()
                .user(fullPrompt)
                .call()
                .content();
    }

    /**
     * 系统通过智能语义理解与结构化信息抽取，实现电子病历的自动化生成与归档。
     */
    @GetMapping("/gne")
    // 关键修改：添加 name = "input"
    public String chatGne(@RequestParam(name = "input", required = false) String input) {
        String userPrompt = promptTemplateService.resolvePrompt(input);
        String fullPrompt = GNE_PROMPT + "\n\n请对以下诊疗记录进行结构化抽取，输出完整EMR字段：\n" + userPrompt;

        return chatClient.prompt()
                .user(fullPrompt)
                .call()
                .content();
    }
}