package com.example.demo.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PurchaseBinding {

    @Output("handlePurchase-out-0")
    MessageChannel handlePurchase();

}
