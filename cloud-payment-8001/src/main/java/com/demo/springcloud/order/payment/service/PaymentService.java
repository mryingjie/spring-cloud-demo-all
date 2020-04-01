package com.demo.springcloud.order.payment.service;

import com.demo.springcloud.commons.entities.Payment;

/**
 * created by Yingjie Zheng at 2020-03-24 11:12
 */
public interface PaymentService {

    int save(Payment payment);

    Payment getPaymentById( Long id);
}
