package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@FeignClient(name = "cart-service")
public interface MailServiceFeignClientPurchase {

    @GetMapping(value = "/cart/{orderNumber}")
    Cart findByOrderNumber(@PathVariable String orderNumber);

    @GetMapping(value = "/cart/cart-item/{id}")
    List<CartItem> findById(@PathVariable Long id);

    @GetMapping(value = "/cart/order-date/{orderDate}")
    List<Cart> findByOrderDate(@PathVariable("orderDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate);

    @GetMapping(value = "cart/is-sending")
    List<CartItem> findByIsSendingFalse();

    @PutMapping(value = "cart/is-sending/{isSending}/{orderNumber}")
    Integer updateIsSendingTrue(@PathVariable("isSending") Boolean isSending, @PathVariable("orderNumber") String orderNumber);


}