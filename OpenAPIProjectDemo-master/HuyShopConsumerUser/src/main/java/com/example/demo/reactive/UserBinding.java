package com.example.demo.reactive;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface UserBinding {
    String SEND_MAIL = "handleSendMail-in-0";

    @Input(SEND_MAIL)
    SubscribableChannel sendMail();




}
