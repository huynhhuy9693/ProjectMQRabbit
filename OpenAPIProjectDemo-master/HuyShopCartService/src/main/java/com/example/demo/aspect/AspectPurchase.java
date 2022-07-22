package com.example.demo.aspect;




import com.example.demo.source.PurchaseBinding;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.Purchase;
import com.example.demo.service.ProductFeignClient;
import com.example.demo.service.ReportFeignClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@EnableBinding(PurchaseBinding.class)
@Aspect
@Configuration
public class AspectPurchase {

    private MessageChannel messageChannel;


    //Output purchaseChanel
    public AspectPurchase(PurchaseBinding binding) {
        messageChannel = binding.handlePurchase();
    }

    @Autowired
    ProductFeignClient productFeignClient;
    @Autowired
    ReportFeignClient reportFeignClient;

    private Logger logger = LoggerFactory.getLogger(AspectPurchase.class);

//    @Before("execution(* com.example.demo.controller.CartController.placeOrder(..))")
//    public void beforePurchase(JoinPoint joinPoint)
//    {
//        logger.info("before purchase");
//    }

//    @AfterThrowing("execution(* com.example.demo.controller.CartController.placeOrder(..))")
//    public void afterPurchaseFalse(JoinPoint joinPoint)
//    {
//        logger.error("purchase false");
//    }

//    @AfterReturning(value = "execution(* com.example.demo.facade.PurchaseFacade.placeOrder(..)) and args(purchase)")
//    public void afterPurchaseSuccess(JoinPoint joinPoint, Purchase purchase)
//    {
//        logger.info("purchase success");
//        for (CartItemDTO cartItem:purchase.getCartDTO().getCartItemDTOList()) {
//            int quantity = cartItem.getQuantity();
//            int quantityShop = productFeignClient.getQuantityById(cartItem.getProductId());
//            int result = (quantityShop - quantity);
//            productFeignClient.updateProductQuantityForId(result, cartItem.getProductId());
//        }
//        logger.info("update success");
//        logger.info("send mail for customer");
//        Message<Purchase> purchaseMessage = MessageBuilder.withPayload(purchase).build();
//        this.messageChannel.send(purchaseMessage);
//        logger.info("send mail ok");
//
//    }

    @AfterReturning(value = "execution(* com.example.demo.service.CartService.deliveryAndUpdate(..)) and args(orderNumber)")
    public void afterUpdateDelivered(JoinPoint joinPoint, String orderNumber)
    {
        logger.info("print invoice" + orderNumber);
        reportFeignClient.showInvoice(orderNumber);
    }

    @Around(value = "execution(* com.example.demo.controller.CartController.placeOrder(..)) and args(purchase)")
    public ResponseEntity<Object> handlePurchase(ProceedingJoinPoint joinPoint, Purchase purchase) throws Throwable {
        logger.info("start Purchase");
        Object purchaseResponese = null;
        if(purchase.getShippingAddress()!=null)
        {
             purchaseResponese = joinPoint.proceed();

            logger.info("Purchase Success");
            for (CartItemDTO cartItem:purchase.getCartDTO().getCartItemDTOList()) {
                int quantity = cartItem.getQuantity();
                int quantityShop = productFeignClient.getQuantityById(cartItem.getProductId());
                int result = (quantityShop - quantity);
                productFeignClient.updateProductQuantityForId(result, cartItem.getProductId());
                logger.info("update success");

                logger.info("send mail for customer");
                Message<Purchase> purchaseMessage = MessageBuilder.withPayload(purchase).build();
                this.messageChannel.send(purchaseMessage);
                logger.info("send mail ok");

                return new ResponseEntity<>(purchaseResponese,HttpStatus.OK);
            }
        }
                 logger.error("address delivery not null");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }


}