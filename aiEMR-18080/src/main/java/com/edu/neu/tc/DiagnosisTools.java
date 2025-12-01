package com.edu.neu.tc;

import com.edu.neu.service.DiagnosisService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Component
public class DiagnosisTools {
    @Autowired
    DiagnosisService service;

    @Tool(description = "通过诊断记录id查询诊断记录内容")
    public String findDiagnosis(
            @ToolParam(description = "诊断记录唯一id") Integer id
    ){
        return service.findById(id);
    }
    /*
    private static final Random random = new Random(System.currentTimeMillis());
    private static final String serviceId = "";
    private static final String Mapping = "";

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

    @Tool(description = "通过nacos微服务调用处方助手子系统的就诊数据，参考数据完成病历")
    public String callDiagnosis(){
        List<ServiceInstance> instances =discoveryClient.getInstances("");
        ServiceInstance instance = instances.get(random.nextInt(instances.size()));
        String url = "http://"+instance.getHost()+":"+instance.getPort()+Mapping;
        return restTemplate.getForObject(url,String.class);
    }
    */
}
