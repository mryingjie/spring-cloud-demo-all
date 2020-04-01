package com.demo.springcloud.consumer;

import com.alibaba.fastjson.JSON;
import com.demo.springcloud.commons.entities.Payment;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * created by Yingjie Zheng at 2020-03-30 10:45
 */
@Component
@EnableBinding(Sink.class)
public class MessageListener {

    @StreamListener(Sink.INPUT)
    public void receiveMessage(Message<Payment> message){

        System.out.println("receive message ["+ JSON.toJSONString(message.getPayload())+"]");

    }
}
