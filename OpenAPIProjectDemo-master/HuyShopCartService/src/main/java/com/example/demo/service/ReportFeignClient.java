package com.example.demo.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "report-service")
public interface ReportFeignClient {

    @GetMapping("/report/cart/invoice/{orderNumber}/{token}")
    void showInvoice(@PathVariable("orderNumber") String orderNumber,@PathVariable("token") String token);
}
