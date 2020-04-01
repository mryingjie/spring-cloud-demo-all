package com.demo.springcloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * created by Yingjie Zheng at 2020-03-25 15:10
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer {


    public static void main(String[] args) {
        SpringApplication.run(EurekaServer.class, args);
    }

}
