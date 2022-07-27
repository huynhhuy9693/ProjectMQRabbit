package com.example.demo.aspect;


import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.stream.function.StreamBridge;
@Aspect
@Configuration
public class UserAOP {

    @Autowired
    UserService service;
    private Logger logger = LoggerFactory.getLogger(UserAOP.class);

    StreamBridge streamBridge;

    public UserAOP(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }


//    @Around(value = "execution(* com.example.demo.service.UserService.save(..)) and args(user)")
//    public void handleSendMail(User user)
//    {
//
//        logger.info("send mail for customer ");
//        streamBridge.send("handleSendMail-in-0", user);
//        logger.info("send mail ok");
//    }
     @Around(value = "execution(* com.example.demo.service.UserService.save(..)) and args(user)")
    public void handleSendMail(ProceedingJoinPoint joinPoint, User user) throws Throwable {
        if(user.getId()==null)
        {
            joinPoint.proceed();
            logger.info("send mail for new customer");
            streamBridge.send("handleSendMail",user);
            logger.info("send mail ok");
        }else
        {
            joinPoint.proceed();
            logger.info("send mail for customer update info");
            streamBridge.send("handleSendMail",user);
            logger.info("send mail ok");
        }
    }





}
