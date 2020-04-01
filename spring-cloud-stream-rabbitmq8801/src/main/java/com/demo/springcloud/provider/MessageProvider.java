package com.demo.springcloud.provider;

/**
 * created by Yingjie Zheng at 2020-03-30 10:04
 */
public interface MessageProvider{

    <T> boolean send(T message);
}
