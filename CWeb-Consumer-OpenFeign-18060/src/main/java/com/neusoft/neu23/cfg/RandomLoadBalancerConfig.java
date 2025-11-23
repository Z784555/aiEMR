package com.neusoft.neu23.cfg;

import com.neusoft.neu23.common.RandomLoadBalancer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomLoadBalancerConfig {

    @Bean
    ReactorServiceInstanceLoadBalancer randomLoadBalancer(
            ObjectProvider<ServiceInstanceListSupplier> provider) {
        // 服务名从配置读取
        return new RandomLoadBalancer(provider, "web-provider");
    }
}
