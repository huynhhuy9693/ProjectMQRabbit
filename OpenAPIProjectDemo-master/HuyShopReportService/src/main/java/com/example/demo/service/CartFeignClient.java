package com.example.demo.service;


import com.example.demo.model.CartDTO;
import com.example.demo.model.CartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name="cart-service")
public interface CartFeignClient {


    @GetMapping(value = "/cart/cart/all")
     List<CartDTO> findAllCart();

    @GetMapping(value = "cart/sum-total-price")
     Long sumTotalPrice();

    @GetMapping(value = "/cart/order-date/{startDate}/{lastDate}")
    List<CartDTO> findByDateOrderBetween(@PathVariable ("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
                                            , @PathVariable("lastDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate,
                                                @RequestHeader("Authorization") String token);

    @GetMapping(value = "cart/sum-total-price/{startDate}/{lastDate}")
    Long sumTotalPriceBetween(@PathVariable ("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
            ,@PathVariable("lastDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate,
                              @RequestHeader("Authorization") String token);


    @GetMapping(value = "/cart/{orderNumber}")
    CartDTO findByOderNumber(@PathVariable("orderNumber") String orderNumber,
                             @RequestHeader("Authorization") String token);

    @GetMapping(value = "/cart/cart-item/cart/{orderNumber}")
    List<CartItemDTO> findItemByOrderNumber(@PathVariable("orderNumber") String orderNumber, @RequestHeader("Authorization") String token);

}
