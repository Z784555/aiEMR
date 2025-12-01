package com.edu.neu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SpringBootApp
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.edu.neu.mapper")
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
        System.out.println("App已运行！");
    }
}
