package com.example.demo.source;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PurchaseBinding {
    String SEND_MAIL_ORDER_SUCCESS = "handlePurchase-in-0";

    @Input(SEND_MAIL_ORDER_SUCCESS)
    SubscribableChannel sendmail();
}
