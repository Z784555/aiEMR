package com.edu.neu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBootApp
 *
 */
@SpringBootApplication
@MapperScan("com.edu.neu.mapper")
public class aiEMRApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(aiEMRApp.class, args);
    }
}
