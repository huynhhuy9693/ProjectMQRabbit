package com.example.demo.service;


import com.example.demo.model.*;
import com.google.gson.Gson;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MailService {
    private JavaMailSender javaMailSender;

    @Autowired
    MailServiceFeignClientPurchase mailServiceFeignClientPurchase;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Autowired
    public MailService(JavaMailSender javaMailSender)
    {
        this.javaMailSender=javaMailSender;
    }

    public void sendMailCreateUser(User user)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("register success " + user.getName() );
        mailMessage.setText("Hello new  user : " + user.getUserName()+"-"+"password : "+user.getPassWord());
        rabbitTemplate.convertAndSend(exchange,routingkey,mailMessage);
    }

    public void sendMailUpdateUser(User user)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("update success " + user.getName() );
        mailMessage.setText("Hello user : " + user.getUserName()+"-"+"password changed success : "+user.getPassWord());
        rabbitTemplate.convertAndSend(exchange,routingkey,mailMessage);
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendMailBeforeOneDayDelievery()
    {
        List<Cart> cartList = mailServiceFeignClientPurchase.findByOrderDate(LocalDate.now().minusDays(4));
        {
            for(Cart cart : cartList)
            {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(cart.getEmail());
                mailMessage.setSubject("thank you : " + cart.getOderNumber());
                mailMessage.setText("Your product will be delivered tomorrow!! Please check your phone ---Thank");
                javaMailSender.send(mailMessage);
                mailServiceFeignClientPurchase.updateIsSendingTrue(true, cart.getOderNumber());
            }
        }
    }


}
