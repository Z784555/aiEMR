package com.neusoft.neu23.fc;

import com.neusoft.neu23.cfg.RandomLoadBalancerConfig;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "web-provider",configuration = {RandomLoadBalancerConfig.class})
public interface ProviderFeignClient {
    @GetMapping("/s/s1")
    String hello();
}
