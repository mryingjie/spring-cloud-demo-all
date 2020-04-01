package com.demo.springcloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * created by Yingjie Zheng at 2020-03-24 15:05
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class OrderServiceClient {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceClient.class, args);
    }


    @Bean
    @LoadBalanced //此注解才能打开RestTemplate 从eureka获得服务发现的功能
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
