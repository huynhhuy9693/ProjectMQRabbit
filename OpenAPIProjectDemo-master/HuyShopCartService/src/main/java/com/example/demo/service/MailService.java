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
        //Gson convert String -> Object
//        Gson gson = new Gson();
//        Purchase purchase = gson.fromJson(jsonPurchase,Purchase.class);
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(purchase.getCartDTO().getEmail());
//        mailMessage.setSubject("thank you : " +orderNumber);
//        mailMessage.setText("thanks you " + purchase.getUserOrder().getUserName()+
//                "-- ToTal price : "+purchase.getCartDTO().getTotalPrice()+
//                "-- Your Order payment "+purchase.getStatus()+
//                "--We will delivery your product in 5 days in " + purchase.getShippingAddress()+
//                "-- Please check invoice details and status in website -- thank you and see your later"
//        );
        try{
            rabbitTemplate.convertAndSend(exchange,routingkey,purchase);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}