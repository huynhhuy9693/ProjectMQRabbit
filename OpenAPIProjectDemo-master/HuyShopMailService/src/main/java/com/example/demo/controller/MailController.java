package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.MailService;
import com.example.demo.service.MailServiceFeignClientPurchase;
import com.example.demo.service.MailServiceFeignClientUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import java.util.Set;


@RestController
@RequestMapping(value = "/mail")
public class MailController {

    @Autowired
    MailServiceFeignClientUser mailServiceFeignClientUser;

    @Autowired
    MailServiceFeignClientPurchase mailServiceFeignClientPurchase;

    @Autowired
    MailService service;

    @Autowired
    ModelMapper modelMapper;

    @Value("${app.message}")
    private String message;


//    @PostMapping(value="/send")
//    public String sendEmailCreateUser(@RequestBody User user) {
//        User modelMail = mailServiceFeignClientUser.createUser(user);
//        user.setEmail(user.getEmail());
//        try{
//            service.sendMailCreateUser(modelMail);
//        }catch (MailException e)
//        {
//            System.out.println(e);
//        }
//        return message;
//    }

//    @PutMapping(value="/send/{id}")
//    public String sendEmailUpdateUser(@RequestBody User user, @PathVariable Long id) {
//        User modelMail = mailServiceFeignClientUser.editUser(id,user);
//        user.setEmail(user.getEmail());
//        try{
//            service.sendMailUpdateUser(modelMail);
//        }catch (MailException e)
//        {
//            System.out.println(e);
//        }
//        return message;
//    }
//    @PostMapping(value="/send/{orderNumber}",consumes = "application/json")
//    public String sendEmailCartSuccess(@PathVariable ("orderNumber") String orderNumber,@RequestBody  String jsonPurchase) {
//        System.out.println("mail-order");
//        try{
//            service.sendMailPurchaseSuccsess(orderNumber,jsonPurchase);
//        }catch (MailException e)
//        {
//            System.out.println(e);
//        }
//        return message;



//    @PostMapping(value="/send/notiBeforeDelivery")
//    public String sendMailBeforeOneDayDelievery(@RequestBody MailModel mailModel) {
//        System.out.println("mail-order");
//        try{
//            service.sendMailBeforeOneDayDelievery();
//        }catch (MailException e)
//        {
//            System.out.println(e);
//        }
//        return message;
//    }



}
