package com.example.demo.service;


import com.example.demo.dto.PaymentDTO;
import com.example.demo.dto.VoucherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service")
public interface PaymentFeignClient {

    @GetMapping(value = "/promotion/voucher/{id}")
    VoucherDTO getById(@PathVariable ("id") int id);

    @GetMapping(value = "/promotion/voucher/name/{name}")
    VoucherDTO getByNameVoucher(@PathVariable ("name") String name);

    @GetMapping(value = "/payment/name/{name}")
    PaymentDTO getByNamePayment(@PathVariable ("name") String name);


}
