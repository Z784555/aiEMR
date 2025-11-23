package com.neusoft.neu23.fc;

import com.neusoft.neu23.cfg.RandomLoadBalancerConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 次接口需要伪装成可以直接访问微服务的客户端
 */
@FeignClient(name = "web-provider",configuration = {RandomLoadBalancerConfig.class})
public interface WebProiverFeignClient {
    /**
     * 模拟调用微服务
     * GetMapping:定义微服务的服务路径
     * @return
     */
    @GetMapping("/s/s1")
    String s1();
}
