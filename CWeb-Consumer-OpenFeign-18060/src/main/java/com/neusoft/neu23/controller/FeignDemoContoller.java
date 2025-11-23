package com.neusoft.neu23.controller;

import com.neusoft.neu23.fc.ProviderFeignClient;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/f")
@CrossOrigin
public class FeignDemoContoller {
    @Autowired
    private ProviderFeignClient providerFeignClient;

    @GetMapping("/call")
    public String callProvider() {
        return providerFeignClient.hello();
    }
}
