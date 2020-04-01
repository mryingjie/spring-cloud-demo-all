package com.demo.springcloud.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * created by Yingjie Zheng at 2020-03-31 17:42
 */

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentNacosClient {

    public static void main(String[] args) {
        SpringApplication.run(PaymentNacosClient.class, args);
    }

}
