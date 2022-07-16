package com.example.demo.service;


import com.example.demo.model.CartReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="cart-service")
public interface CartFeignClient {


    @GetMapping(value = "/cart/cart/all")
    public List<CartReport> findAllCart();

    @GetMapping(value = "cart/sum-total-price")
    public Long sumTotalPrice();
}
