package com.example.demo.reactive;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CartBinding {
    String DATA_SYNC_FROM_CART= "dataSyncFromCart-in-0";


    @Input(DATA_SYNC_FROM_CART)
    SubscribableChannel dataSyncFromCart();




}
