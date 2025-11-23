package com.neusoft.neu23;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class DWebGate18050App
{
    public static void main( String[] args )
    {
        SpringApplication.run(DWebGate18050App.class, args);
        System.out.println( "DWebGate18050App" );
    }
}
