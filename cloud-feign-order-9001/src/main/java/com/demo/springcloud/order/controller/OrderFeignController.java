package com.demo.springcloud.order.controller;

import com.demo.springcloud.commons.entities.CommonResult;
import com.demo.springcloud.commons.entities.Payment;
import com.demo.springcloud.order.feign.PaymentFeignService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.guice.EurekaModule;
import com.netflix.discovery.shared.Application;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


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

    @Value("${spring.application.name}")
    private String appName;



    @GetMapping("send")
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendRedirect("/order/1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/create")
    public CommonResult create(Payment payment){
        return paymentFeignService.create(payment);
    }

    @GetMapping(value = "/{id}")
    public CommonResult getPayment(@PathVariable(value = "id") Long id, HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("waliyun-account");
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        System.out.println(requestURL.toString());

        CommonResult<Payment> result = paymentFeignService.getById(id);
        Application application = DiscoveryManager.getInstance().getEurekaClient().getApplication(appName);
        int size = application.size();
        List<InstanceInfo> instances = application.getInstances();
        return result;
    }


    @PostMapping("/test")
    @ResponseBody
    public CommonResult testDesensitization(@RequestBody Payment payment){



        System.out.println(payment);

        return new CommonResult(200, null,payment);
    }
    @PostMapping("/mapParamTest")
    @ResponseBody
    public CommonResult<Map> mapParamTest(@RequestBody Map<String,Object> map){
        System.out.println(map);
        return new CommonResult<>(200,"",map );
    }

    @PostMapping("/testNullParam")
    @ResponseBody
    public CommonResult testNullParam(){
        return new CommonResult(200, "success", new Payment());
    }


    @GetMapping(value = "/timeout/{id}")
    @HystrixCommand(
            fallbackMethod = "getByIdTimeoutFallBack",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public CommonResult<Payment> getByIdTimeout(@PathVariable(value = "id",required = true) Long id){
        return paymentFeignService.getById(id);
    }

    public CommonResult<Payment> getByIdTimeoutFallBack(Long id) {
        return new CommonResult<>(8002, "服务繁忙，o(╥﹏╥)o");
    }

    public CommonResult<Payment> defaultFallback() {
        return new CommonResult<>(8003, "服务开小差了，o(╥﹏╥)o ");
    }

}
