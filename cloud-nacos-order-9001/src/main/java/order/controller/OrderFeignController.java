package order.controller;

import com.demo.springcloud.commons.entities.CommonResult;
import com.demo.springcloud.commons.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import order.feign.PaymentFeignService;
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




    public CommonResult<Payment> getByIdTimeoutFallBack(Long id) {
        return new CommonResult<>(8002, "服务繁忙，o(╥﹏╥)o");
    }

    public CommonResult<Payment> defaultFallback() {
        return new CommonResult<>(8003, "服务开小差了，o(╥﹏╥)o ");
    }

}
