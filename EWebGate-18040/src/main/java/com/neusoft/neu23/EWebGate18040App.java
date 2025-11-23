package com.neusoft.neu23;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EWebGate18040App
{
    public static void main( String[] args )
    {
        SpringApplication.run(EWebGate18040App.class, args);
        System.out.println( "Hello World!" );
    }
}
