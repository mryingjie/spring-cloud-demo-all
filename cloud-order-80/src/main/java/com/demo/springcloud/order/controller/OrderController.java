package com.demo.springcloud.order.controller;

import com.demo.springcloud.commons.entities.CommonResult;
import com.demo.springcloud.commons.entities.Payment;
import com.demo.springcloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Payment)表控制层
 *
 * @author makejava
 * @since 2020-03-24 15:13:34
 */
@RestController
@RequestMapping("order")
@Slf4j
public class OrderController {

    public static final String HTTP_URL = "http://CLOUD-PAYMENT-SERVICE";

    /**
     * 服务对象
     */
    @Resource
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 通过主键查询单条数据
     *
     * @return 单条数据
     */

    @GetMapping(value = "create")
    public CommonResult create(Payment payment){
       return restTemplate.postForObject(HTTP_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping(value = "{id}")
    public CommonResult getPayment(@PathVariable(value = "id") Long id){
        return restTemplate.getForObject(HTTP_URL+"/payment/"+id,CommonResult.class );
    }

    @GetMapping(value = "/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("****element: " +element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance:instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }



}