package com.demo.springcloud.order.payment.mapper;

import com.demo.springcloud.commons.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * created by Yingjie Zheng at 2020-03-24 10:56
 */
@Mapper
public interface PaymentMapper {


    int save(Payment payment);

    Payment getPaymentById(@Param("id") Long id);

}
