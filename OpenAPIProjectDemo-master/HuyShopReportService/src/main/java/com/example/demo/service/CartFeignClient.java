package com.example.demo.service;


import com.example.demo.model.CartReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name="cart-service")
public interface CartFeignClient {


    @GetMapping(value = "/cart/cart/all")
     List<CartReport> findAllCart();

    @GetMapping(value = "cart/sum-total-price")
     Long sumTotalPrice();

    @GetMapping(value = "/cart/order-date/{startDate}/{lastDate}")
    List<CartReport> findByDateOrderBetween(@PathVariable ("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
                                            ,@PathVariable("lastDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate);

    @GetMapping(value = "cart/sum-total-price/{startDate}/{lastDate}")
    Long sumTotalPriceBetween(@PathVariable ("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
            ,@PathVariable("lastDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate);
}
