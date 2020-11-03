package com.demo.springcloud.order.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * created by Yingjie Zheng at 2020/4/17 14:15
 */
@Aspect
@Component
public class PermissionAspect {


    @Pointcut("execution(public * com.demo.springcloud.order.controller.*.*(..))")
    public void webPointCut() {
    }


    // @Around("webPointCut()")
    // public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
    //     String test = cookieService.Test();
    //
    //     try {
    //         CasAccountLoginManager.setAccount(Thread.currentThread().getName() + "---account");
    //         Object proceed = pjp.proceed();
    //         return proceed;
    //     } finally {
    //         CasAccountLoginManager.removeAccount();
    //     }
    // }

}


