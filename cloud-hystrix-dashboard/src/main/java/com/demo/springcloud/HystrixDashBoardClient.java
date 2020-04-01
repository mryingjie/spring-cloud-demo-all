package com.demo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * created by Yingjie Zheng at 2020-03-26 17:03
 */
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashBoardClient {


    public static void main(String[] args) {

        SpringApplication.run(HystrixDashBoardClient.class, args);
    }

}
