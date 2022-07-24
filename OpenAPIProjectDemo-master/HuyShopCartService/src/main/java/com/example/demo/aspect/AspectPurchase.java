package com.example.demo.aspect;



import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.Purchase;
import com.example.demo.dto.PurchaseResponse;
import com.example.demo.service.ProductFeignClient;
import com.example.demo.service.ReportFeignClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

import java.util.List;


@Aspect
@Configuration
//@EnableBinding(PurchaseBinding.class)
public class AspectPurchase {

    private MessageChannel messageChannel;


    //Output purchaseChanel
//    public AspectPurchase(PurchaseBinding binding) {
//        messageChannel = binding.handlePurchase();
//    }


    @Autowired
    ProductFeignClient productFeignClient;
    @Autowired
    ReportFeignClient reportFeignClient;

    private Logger logger = LoggerFactory.getLogger(AspectPurchase.class);

    StreamBridge streamBridge;

    public AspectPurchase(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }
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
        //before
        logger.info("start Purchase");
        Object purchaseResponese = null;

        if (purchase.getShippingAddress() != null
                && purchase.getCartDTO().getUserOrder() != null
                && purchase.getCartDTO().getCartItemDTOList() != null) {
            //execution method placeOrder
            purchaseResponese = joinPoint.proceed();

            //after
            if (purchase.getStatus().equalsIgnoreCase("FALSE"))
            {
                logger.info("send mail for admin info purchase false");
                streamBridge.send("handleAfterOrderFalse", purchase);

            }else
            {
                logger.info("Purchase Success");
                for (CartItemDTO cartItem : purchase.getCartDTO().getCartItemDTOList()) {
                    int quantity = cartItem.getQuantity();
                    int quantityShop = productFeignClient.getQuantityById(cartItem.getProductId());
                    int result = (quantityShop - quantity);
                    productFeignClient.updateProductQuantityForId(result, cartItem.getProductId());
                    logger.info("update success");
                    logger.info("send mail for customer");
//                Message<Purchase> purchaseMessage = MessageBuilder.withPayload(purchase).build();
//                this.messageChannel.send(purchaseMessage);
                    streamBridge.send("handlePurchase", purchase);
                    logger.info("send mail ok");
                    logger.info("send mail for admin ");
                    streamBridge.send("handleAfterOrderSuccess", purchase);
                    logger.info("send mail ok");

                    return new ResponseEntity<>(purchaseResponese, HttpStatus.OK);
                }
            }

        } else if (purchase.getCartDTO().getUserOrder() == null) {
            logger.error(" User not null ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (purchase.getShippingAddress() == null) {
            logger.error(" Address not null ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else if (purchase.getShippingAddress() == null)
        {
            logger.error("Cart-Item  not null ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

