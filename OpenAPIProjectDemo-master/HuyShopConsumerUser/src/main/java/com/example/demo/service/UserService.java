package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.reactive.UserBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(UserBinding.class)
public class UserService {

    private JavaMailSender mailSender;

    @Autowired
    StreamBridge streamBridge;
    @Autowired
    public UserService(JavaMailSender javaMailSender)
    {
        this.mailSender=javaMailSender;
    }
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);



    @StreamListener(target = UserBinding.SEND_MAIL)
    public void handleSendMail(User user) throws Exception {

        if(user.getName()==null)
        {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(" Thank you ");
            mailMessage.setText("Welcome " + user.getUserName());
            mailSender.send(mailMessage);
            logger.info("send mail ok");
            streamBridge.send("handleAfterSendMail",user);
        }else
        {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Admin notification update info user : " + user.getName());
            mailMessage.setText("Info updated success ");
            mailSender.send(mailMessage);
            logger.info("send mail ok");
            streamBridge.send("handleAfterSendMail",user);

        }


    }

}
