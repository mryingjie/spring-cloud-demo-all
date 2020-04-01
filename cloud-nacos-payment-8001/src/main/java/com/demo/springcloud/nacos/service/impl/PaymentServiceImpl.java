package com.demo.springcloud.nacos.service.impl;

import com.demo.springcloud.commons.entities.Payment;
import com.demo.springcloud.nacos.mapper.PaymentMapper;
import com.demo.springcloud.nacos.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * created by Yingjie Zheng at 2020-03-24 11:12
 */
@Service
public class PaymentServiceImpl implements PaymentService {


    @Resource
    private PaymentMapper paymentMapper;

    @Override
    public int save(Payment payment) {
        return paymentMapper.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentMapper.getPaymentById(id);
    }
}
