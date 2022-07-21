package com.example.demo.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PurchaseBinding {

    @Output("purchaseChannel")
    MessageChannel handlePurchase();

}
