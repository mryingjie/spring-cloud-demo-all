package order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * created by Yingjie Zheng at 2020-03-25 17:36
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class OrderNacosFeignClient {


    public static void main(String[] args) {

        SpringApplication.run(OrderNacosFeignClient.class, args);
    }




}
