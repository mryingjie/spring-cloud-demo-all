package com.demo.springcloud.zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * created by Yingjie Zheng at 2020/4/9 11:13
 * 限流Filter
 */
@Component
public class RateLimiterFilter extends ZuulFilter {

    //限流桶实现 每秒钟100个令牌
    private static final RateLimiter RATELIMITER= RateLimiter.create(1);
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //配置优先级最高
        return SERVLET_DETECTION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //判断是否取到令牌
        if (!RATELIMITER.tryAcquire()) {
            //没有取到令牌 直接抛出异常
            throw new RuntimeException();
        }
        return "acquire success";
    }
}
