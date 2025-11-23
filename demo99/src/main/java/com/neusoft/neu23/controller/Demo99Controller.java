package com.neusoft.neu23.controller;

import com.neusoft.neu23.tc.DateTimeTools;
import com.neusoft.neu23.tc.MedicalTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import static com.neusoft.neu23.cfg.AiConfig.TOOLCALLING_PROMPT;

/**
 * 医疗AI导诊系统控制器
 */
@RequestMapping("/d")
@RestController
@CrossOrigin
public class Demo99Controller {
    private ChatClient chatClient;
    
    public Demo99Controller(OpenAiChatModel openAiChatModel, ChatMemory chatMemory,
                            DateTimeTools dateTimeTools,
                            MedicalTools medicalTools
    ) {
        this.chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(TOOLCALLING_PROMPT) // 医疗导诊系统提示词
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(dateTimeTools)  // 时间工具
                .defaultTools(medicalTools)   // 医疗导诊工具
                .build();
    }
    
    /**
     * 医疗AI导诊接口
     * @param msg 患者输入的消息
     * @param chatId 会话ID，用于保持对话上下文
     * @return AI助手的回复
     */
    @GetMapping("/c1")
    public String chat1(@RequestParam(value = "msg", defaultValue = "你好，我需要看病") String msg,
                        @RequestParam(value = "chatId", defaultValue = "medical-consultation") String chatId
    ) {
        return this.chatClient.prompt()
                .user(msg)  // 提交给大模型的患者消息
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content();
    }

}
