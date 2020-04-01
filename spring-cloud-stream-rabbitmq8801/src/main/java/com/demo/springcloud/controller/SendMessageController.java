package com.demo.springcloud.controller;

import com.alibaba.fastjson.JSON;
import com.demo.springcloud.commons.entities.CommonResult;
import com.demo.springcloud.commons.entities.Payment;
import com.demo.springcloud.provider.MessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * created by Yingjie Zheng at 2020-03-30 10:28
 */
@RestController
public class SendMessageController {


    @Resource
    private MessageProvider messageProvider;


    @GetMapping("/send")
    public CommonResult send(Payment payment){
        if (messageProvider.send(payment)) {
            return new CommonResult(200,"消息发送成功",JSON.toJSONString(payment));
        }
        return new CommonResult(500,"消息发送失败");
    }



}
