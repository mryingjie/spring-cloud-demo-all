package com.demo.springcloud.order.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * created by Yingjie Zheng at 2020-03-23 17:42
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient  //启用DiscoveryClient  可以直接注入到类里
@EnableCircuitBreaker
public class PaymentServiceClient {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceClient.class, args);
    }

}
