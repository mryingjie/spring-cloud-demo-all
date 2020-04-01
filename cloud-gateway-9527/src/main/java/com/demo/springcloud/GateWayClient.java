package com.demo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * created by Yingjie Zheng at 2020-03-27 15:05
 */
@SpringBootApplication
@EnableEurekaClient
public class GateWayClient {

    public static void main(String[] args) {
        SpringApplication.run(GateWayClient.class, args);
    }

}
