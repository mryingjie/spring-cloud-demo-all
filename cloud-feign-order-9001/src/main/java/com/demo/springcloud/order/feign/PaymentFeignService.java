package com.demo.springcloud.order.feign;

import com.demo.springcloud.commons.entities.CommonResult;
import com.demo.springcloud.commons.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by Yingjie Zheng at 2020-03-25 17:41
 */
@Component
@FeignClient(value = "cloud-payment-service",fallback = PaymentFeignServiceFallback.class)
public interface PaymentFeignService {

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment);


    @GetMapping(value = "/payment/{id}")
    public CommonResult<Payment> getById(@PathVariable(value = "id",required = true) Long id);



    @GetMapping(value = "/payment/timeout/{id}")
    public CommonResult<Payment> getByIdTimeout(@PathVariable(value = "id",required = true) Long id);

}
