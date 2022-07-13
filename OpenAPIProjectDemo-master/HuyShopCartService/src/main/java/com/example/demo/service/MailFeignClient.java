package com.example.demo.service;


import com.example.demo.dto.Purchase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-service" )
public interface MailFeignClient {

    @PostMapping(value = "/mail/send/{orderNumber}",consumes = "application/json" )
    void sendMailSuccess(@PathVariable ("orderNumber") String orderNumber,@RequestBody String jsonPurchase);

    @PostMapping(value = "/mail/send/{userName}" )
    void sendMailBeforeOneDayDelivery(@PathVariable ("userName") String userName,@RequestBody String jsonPurchase);

    @PostMapping(value = "/mail/send/notiBeforeDelivery")
    void sendMailNotiBeforeDeli();

}
