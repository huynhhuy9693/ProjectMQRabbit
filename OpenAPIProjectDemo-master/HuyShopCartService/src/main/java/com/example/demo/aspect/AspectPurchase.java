package com.example.demo.aspect;




import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.Purchase;
import com.example.demo.dto.PurchaseResponse;
import com.example.demo.service.ProductFeignClient;
import org.aspectj.lang.JoinPoint;
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

    @Autowired
    ProductFeignClient productFeignClient;

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
    @AfterReturning(value = "execution(* com.example.demo.controller.CartController.placeOrder(..)) and args(purchase)")
    public void afterPurchaseSuccess(JoinPoint joinPoint, Purchase purchase)
    {
        logger.info("purchase success");
        for (CartItemDTO cartItem:purchase.getCartDTO().getCartItemDTOList()) {
            int quantity = cartItem.getQuantity();
            int quantityShop = productFeignClient.getQuantityById(cartItem.getProductId());
            int result = (quantityShop - quantity);
            productFeignClient.updateProductQuantityForId(result, cartItem.getProductId());
        }
        logger.info("update success");
        logger.info("send mail for customer");
        rabbitTemplate.convertAndSend(exchange,routingkey,purchase);
        logger.info("send mail ok");

    }



}