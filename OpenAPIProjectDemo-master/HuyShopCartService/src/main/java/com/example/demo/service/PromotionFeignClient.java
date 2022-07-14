package com.example.demo.service;


import com.example.demo.dto.VoucherDTO;
import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "promotion-service")
public interface PromotionFeignClient {

    @GetMapping(value = "/promotion/voucher/{id}")
    VoucherDTO getById(@PathVariable ("id") int id);

    @GetMapping(value = "/promotion/voucher/name/{name}")
    VoucherDTO getByName(@PathVariable ("name") String name);


}
