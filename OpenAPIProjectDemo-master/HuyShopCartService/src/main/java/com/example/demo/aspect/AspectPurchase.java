package com.example.demo.aspect;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AspectPurchase {

    private Logger logger = LoggerFactory.getLogger(AspectPurchase.class);

//    @Before("execution(* com.example.demo.controller.CartController.placeOrder(..))")
//    public void beforePurchase(JoinPoint joinPoint)
//    {
//        logger.info("before + " +joinPoint.toString());
//        logger.info("before purchase");
//    }

    @AfterThrowing("execution(* com.example.demo.controller.CartController.placeOrder(..))")
    public void afterPurchaseFalse(JoinPoint joinPoint)
    {
        logger.info("purchase false");
    }
//    @AfterReturning("execution(* com.example.demo.controller.CartController.placeOrder(..))")
//    public void afterPurchaseSuccess(JoinPoint joinPoint)
//    {
//        logger.info("purchase success");
//    }


    @Around("execution(* com.example.demo.controller.CartController.placeOrder(..))")
    public void purchaseSuccess(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("start purchase");
        System.out.println(joinPoint.proceed().toString());
        joinPoint.proceed();
        logger.info("purchase success");

    }
}
