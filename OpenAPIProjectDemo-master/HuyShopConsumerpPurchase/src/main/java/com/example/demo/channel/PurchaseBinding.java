package com.example.demo.channel;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PurchaseBinding {
    String SEND_MAIL_ORDER_SUCCESS = "purchaseChannel";

    @Input(SEND_MAIL_ORDER_SUCCESS)
    SubscribableChannel sendmail();
}
