package com.example.demo.aspect;




import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.Purchase;
import com.example.demo.entity.CartEntity;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.ProductFeignClient;
import com.example.demo.service.ReportFeignClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;



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
    CartRepository repository;

    @Autowired
    ProductFeignClient productFeignClient;
    @Autowired
    ReportFeignClient reportFeignClient;

    private Logger logger = LoggerFactory.getLogger(AspectPurchase.class);

    StreamBridge streamBridge;

    public AspectPurchase(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Before("execution(* com.example.demo.controller.CartController.placeOrder(..))")
    public void beforePurchase(JoinPoint joinPoint)
    {
        logger.info("before purchase");
    }

    @AfterThrowing("execution(* com.example.demo.controller.CartController.placeOrder(..))")
    public void afterPurchaseFalse(JoinPoint joinPoint)
    {
        logger.error("purchase false");
    }

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
//        streamBridge.send("handlePurchase", purchase);
//        logger.info("send mail ok");
//
//    }

//    @AfterReturning(value = "execution(* com.example.demo.service.CartService.deliveryAndUpdate(..)) and args(orderNumber,token)")
//    public void afterUpdateDelivered(JoinPoint joinPoint, String orderNumber,String token) throws Throwable {
//
//
//        long startTime = System.currentTimeMillis();
//        CartEntity cart = repository.findByOderNumber(orderNumber);
//        logger.info("execution time handle print : " + ((System.currentTimeMillis() - startTime )/1000f));
//    }

    @Around(value = "execution(* com.example.demo.controller.CartController.placeOrder(..)) and args(purchase)")
    public Object handlePurchase(ProceedingJoinPoint joinPoint, Purchase purchase) throws Throwable {
        //before
        long startTime = System.currentTimeMillis();
        logger.info("start Purchase");

        Object purchaseResponese = null;

        if (purchase.getCartDTO().getUserOrder() == null) {
            logger.error(" User not null ");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (purchase.getShippingAddress() == null) {
            logger.error(" Address not null ");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (purchase.getShippingAddress() == null) {
            logger.error("Cart-Item  not null ");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            purchaseResponese = joinPoint.proceed();
            if (purchase.getStatus().equalsIgnoreCase("FALSE")) {
                purchase.getCartDTO().getUserOrder().setEmail("huynhhuy362022@gmail.com");
                logger.info("send mail for admin info purchase false");
                streamBridge.send("handlePurchase", purchase);
                logger.info("send mail ok");

            } else
            {
                logger.info("Purchase Success");
                for (CartItemDTO cartItem : purchase.getCartDTO().getCartItemDTOList()) {
                    if(cartItem.isCheck()==true) {
                        int quantity = cartItem.getQuantity();
                        int quantityShop = productFeignClient.getQuantityById(cartItem.getProductId());
                        int result = (quantityShop - quantity);
                        productFeignClient.updateProductQuantityForId(result, cartItem.getProductId());
                    }
                }
                logger.info("update success");

                logger.info("send mail for customer");
                streamBridge.send("handlePurchase", purchase);
                logger.info("send mail ok");

                logger.info("send mail for admin ");
                purchase.getCartDTO().getUserOrder().setEmail("huynhhuy362022@gmail.com");
                streamBridge.send("handlePurchase", purchase);
                logger.info("send mail ok");

                logger.info("execution time handle purchase : " + ((System.currentTimeMillis() - startTime) / 1000f));

                return purchaseResponese;
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}

