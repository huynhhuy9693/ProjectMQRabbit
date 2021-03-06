package com.example.demo.service;


import com.example.demo.source.PurchaseBinding;
import com.example.demo.model.Purchase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(PurchaseBinding.class)
public class MailService {

    private JavaMailSender mailSender;
    @Autowired
    public MailService(JavaMailSender javaMailSender)
    {
        this.mailSender=javaMailSender;
    }
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
//    @Override
//    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
//
//    }

    @StreamListener(target = PurchaseBinding.SEND_MAIL_ORDER_SUCCESS)
    public void handlePurchase(Purchase purchase) throws Exception {
        String invoice = "http://localhost:8080/report/cart/invoice/";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(purchase.getCartDTO().getEmail());
        mailMessage.setSubject("thank you : " +purchase.getCartDTO().getOderNumber());
        mailMessage.setText("thanks you " + purchase.getCartDTO().getUserOrder().getName()+
                "-- ToTal price : "+purchase.getCartDTO().getTotalPrice()+
                "-- Your Order payment "+purchase.getStatus()+
                "--We will delivery your product in 5 days in " + purchase.getShippingAddress()+
                "-- Please check invoice details -- thank you and see your later---"
                +invoice+purchase.getCartDTO().getOderNumber());
        mailSender.send(mailMessage);
    }
}