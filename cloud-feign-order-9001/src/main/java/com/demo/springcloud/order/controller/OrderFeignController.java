package com.demo.springcloud.order.controller;

import com.demo.springcloud.commons.entities.CommonResult;
import com.demo.springcloud.commons.entities.Payment;
import com.demo.springcloud.order.feign.PaymentFeignService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * created by Yingjie Zheng at 2020-03-25 17:47
 */
@RestController
@RequestMapping("/order")
@Slf4j
@DefaultProperties(defaultFallback = "defaultFallback")
public class OrderFeignController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    /**
     * 通过主键查询单条数据
     *
     * @return 单条数据
     */

    @GetMapping(value = "/create")
    public CommonResult create(Payment payment){
        return paymentFeignService.create(payment);
    }

    @GetMapping(value = "/{id}")
    public CommonResult getPayment(@PathVariable(value = "id") Long id){
        return paymentFeignService.getById(id);
    }


    @GetMapping(value = "/timeout/{id}")
    @HystrixCommand(fallbackMethod = "getByIdTimeoutFallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public CommonResult<Payment> getByIdTimeout(@PathVariable(value = "id",required = true) Long id){
        return paymentFeignService.getByIdTimeout(id);
    }

    public CommonResult<Payment> getByIdTimeoutFallBack(Long id) {
        return new CommonResult<>(8002, "服务繁忙，o(╥﹏╥)o");
    }

    public CommonResult<Payment> defaultFallback() {
        return new CommonResult<>(8003, "服务开小差了，o(╥﹏╥)o ");
    }

}
