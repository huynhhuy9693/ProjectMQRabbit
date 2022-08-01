package com.example.demo.service;



import com.example.demo.model.Purchase;
import com.example.demo.reactive.PurchaseBinding;
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


    @StreamListener(target = PurchaseBinding.SEND_MAIL_ORDER_SUCCESS)
    public void handlePurchase(Purchase purchase) throws Exception {
        String email = purchase.getCartDTO().getUserOrder().getEmail();
        String orderNumber = purchase.getCartDTO().getOderNumber();
        String userName = purchase.getCartDTO().getUserOrder().getUserName();

        if(email.equalsIgnoreCase("huynhhuy362022@gmail.com"))
        {
            if (purchase.getStatus().equalsIgnoreCase("SUCCESS"))
            {

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("order-success");
                mailMessage.setText("ORDER-NUMBER-SUCCESS : "+orderNumber);
                mailSender.send(mailMessage);
            }else
            {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo( email);
                mailMessage.setSubject("order-false");
                mailMessage.setText("user " + userName+"\n"
                        +"---- email :"+email+"\n"
                        +"---- purchase-status : " +purchase.getStatus()+"\n"
                        +"---- order-number : " +orderNumber+"\n"
                );
                mailSender.send(mailMessage);
            }
        }else {
            String invoice = "http://localhost:8080/report/cart/invoice/";
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("thank you : " +purchase.getCartDTO().getOderNumber());
            mailMessage.setText("thanks you " + purchase.getCartDTO().getUserOrder().getName()+"\n"+
                    "-- ToTal price : "+purchase.getCartDTO().getTotalPrice()+"\n"+
                    "-- Your Order payment "+purchase.getStatus()+"\n"+
                    "--We will delivery your product in 5 days in " + purchase.getShippingAddress()+"\n"+
                    "-- Please check invoice details" +"\n" +
                    "-- thank you and see your later---"
                    +invoice+purchase.getCartDTO().getOderNumber());
            mailSender.send(mailMessage);
        }


    }

//    @StreamListener(target = PurchaseBinding.SEND_ORDER_NUMBER_FOR_ADMIN)
//    public void handleAfterOrderSuccess(Purchase purchase)
//    {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo("huynhhuy362022@gmail.com");
//        mailMessage.setSubject("order-success");
//        mailMessage.setText("ORDER-NUMBER-SUCCESS : "+purchase.getCartDTO().getOderNumber());
//        mailSender.send(mailMessage);
//    }

//    @StreamListener(target = PurchaseBinding.SEND_INFO_PURCHASE_FALSE)
//    public void handleAfterOrderFalse(Purchase purchase)
//    {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo("huynhhuy362022@gmail.com");
//        mailMessage.setSubject("order-false");
//        mailMessage.setText("user " + purchase.getCartDTO().getUserOrder().getUserName()
//                +"---- email :"+purchase.getCartDTO().getEmail()
//                +"---- purchase-status : " +purchase.getStatus()
//                +"---- order-number : " +purchase.getCartDTO().getOderNumber()
//        );
//        mailSender.send(mailMessage);
//    }
}