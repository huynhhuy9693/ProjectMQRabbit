package com.example.demo.aspect;



import com.example.demo.dto.Purchase;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AspectPurchase {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;
    private Logger logger = LoggerFactory.getLogger(AspectPurchase.class);

    @Before("execution(* com.example.demo.controller.CartController.placeOrder(..))")
    public void beforePurchase(JoinPoint joinPoint)
    {
        logger.info("before purchase");
    }

    @AfterThrowing("execution(* com.example.demo.controller.CartController.placeOrder(..))")
    public void afterPurchaseFalse(JoinPoint joinPoint)
    {
        logger.info("purchase false");
    }
    @AfterReturning("execution(* com.example.demo.controller.CartController.placeOrder(..))")
    public void afterPurchaseSuccess(JoinPoint joinPoint)
    {
        logger.info("purchase success");
    }


//    @Around("execution(* com.example.demo.controller.CartController.placeOrder(..)")
//    public void purchaseSuccess(ProceedingJoinPoint joinPoint) throws Throwable {
//        logger.info("start purchase");
//        Purchase purchase = (Purchase) joinPoint.proceed();
//        logger.info("send mail");
//        rabbitTemplate.convertAndSend(exchange,routingkey,purchase);
//        logger.info("purchase success");
//
//
//    }
}
