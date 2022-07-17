package com.example.demo.service;

import com.example.demo.dto.Purchase;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    public void sendMailPurchaseSuccsess( Purchase purchase)
    {
        try{
            rabbitTemplate.convertAndSend(exchange,routingkey,purchase);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}