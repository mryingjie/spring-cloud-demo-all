package order.feign;

import com.demo.springcloud.commons.entities.CommonResult;
import com.demo.springcloud.commons.entities.Payment;
import org.springframework.stereotype.Component;

/**
 * created by Yingjie Zheng at 2020-03-26 16:13
 */
@Component
public class PaymentFeignServiceFallback implements PaymentFeignService{
    @Override
    public CommonResult create(Payment payment) {
        return null;
    }

    @Override
    public CommonResult<Payment> getById(Long id) {
        return null;
    }

    @Override
    public CommonResult<Payment> getByIdTimeout(Long id) {
        return null;
    }
}
