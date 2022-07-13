package com.example.demo.service;


import com.example.demo.model.Purchase;
import com.example.demo.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component

public class MailService implements RabbitListenerConfigurer {

    private JavaMailSender mailSender;
    @Autowired
    public MailService(JavaMailSender javaMailSender)
    {
        this.mailSender=javaMailSender;
    }
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void doSend(Purchase purchase) throws Exception {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(purchase.getCartDTO().getEmail());
        mailMessage.setSubject("thank you : " +purchase.getCartDTO().getOderNumber());
        mailMessage.setText("thanks you " + purchase.getUserOrder().getUserName()+
                "-- ToTal price : "+purchase.getCartDTO().getTotalPrice()+
                "-- Your Order payment "+purchase.getStatus()+
                "--We will delivery your product in 5 days in " + purchase.getShippingAddress()+
                "-- Please check invoice details and status in website -- thank you and see your later"
        );
        mailSender.send(mailMessage);

    }

}
