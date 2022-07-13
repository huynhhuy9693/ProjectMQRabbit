package com.example.demo.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cart-service")
public interface CartItemFeignClient {

    @GetMapping(value = "/cart/cart-item/product-id/{productId}")
    Integer countCartItemByProductId(@PathVariable Long productId);
}
