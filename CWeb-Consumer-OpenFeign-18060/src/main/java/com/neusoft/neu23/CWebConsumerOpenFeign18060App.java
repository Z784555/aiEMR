package com.neusoft.neu23;

import com.neusoft.neu23.cfg.RandomLoadBalancerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
// （3/3）启动服务注册中心
@EnableDiscoveryClient
@EnableFeignClients
@LoadBalancerClients(defaultConfiguration = RandomLoadBalancerConfig.class)
public class CWebConsumerOpenFeign18060App
{
    public static void main( String[] args )
    {
        SpringApplication.run(CWebConsumerOpenFeign18060App.class, args);
        System.out.println( "Hello World!" );
    }
}
