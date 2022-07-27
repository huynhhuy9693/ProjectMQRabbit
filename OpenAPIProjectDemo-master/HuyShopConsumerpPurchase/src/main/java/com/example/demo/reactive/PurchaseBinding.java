package com.example.demo.reactive;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PurchaseBinding {
    String SEND_MAIL_ORDER_SUCCESS = "handlePurchase-in-0";
    String SEND_ORDER_NUMBER_FOR_ADMIN="handleAfterOrderSuccess-in-0";
    String SEND_INFO_PURCHASE_FALSE ="handleAfterOrderFalse-in-0";

    @Input(SEND_MAIL_ORDER_SUCCESS)
    SubscribableChannel sendmail();

    @Input(SEND_ORDER_NUMBER_FOR_ADMIN)
    SubscribableChannel sendOrderNumber();

    @Input(SEND_INFO_PURCHASE_FALSE)
    SubscribableChannel sendInfoPurchaseFalse();


}
