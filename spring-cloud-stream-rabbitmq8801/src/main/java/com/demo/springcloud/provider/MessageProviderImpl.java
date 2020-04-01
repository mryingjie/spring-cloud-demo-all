package com.demo.springcloud.provider;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * created by Yingjie Zheng at 2020-03-30 10:05
 */
@EnableBinding(Source.class)
public class MessageProviderImpl implements MessageProvider {


    @Resource
    private MessageChannel output;

    @Autowired
    private Source source;

    @Override
    public <T> boolean send(T t) {
        System.out.println("send message ["+ JSON.toJSONString(t)+"]");
        Message<T> message = MessageBuilder.withPayload(t).build();
        boolean send = output.send(message);
        return send;
    }
}
