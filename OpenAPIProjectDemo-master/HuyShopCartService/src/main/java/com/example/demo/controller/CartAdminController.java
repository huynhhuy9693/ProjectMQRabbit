package com.example.demo.controller;


import com.example.demo.api.CartApi;
import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/cart")
public class CartAdminController implements CartApi {

    @Autowired
    CartService cartService;
    public ResponseEntity<List<Cart>> getAllCart()
    {
        List<Cart> cartList = cartService.findAll();
        return  new ResponseEntity<>(cartList, HttpStatus.OK);
    }

    @GetMapping(value = "/sum-total-price")
    public ResponseEntity<Long> sumTotalPrice()
    {
        Long result = cartService.sumTotalPrice();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping(value = "/order-date/{orderDate}")
    public ResponseEntity<List<Cart>> findByOrderDate(@PathVariable("orderDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate)
    {
        System.out.println("a");
        return new ResponseEntity<>(cartService.findByOrderDate(orderDate),HttpStatus.OK);
    }

    @GetMapping(value = "/is-sending")
    public ResponseEntity<List<Cart>> findByIsSending()
    {
        List<Cart> cartList = cartService.findByIsSending();
        return new ResponseEntity<>(cartList,HttpStatus.OK);

    }

    @GetMapping(value = "/order-date/{startDate}/{lastDate}")
    public ResponseEntity<List<Cart>> findByDateOrderBetween(@PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                             @PathVariable("lastDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate                            )
    {
        return new ResponseEntity<>(cartService.findByDateOrderBetween(startDate,lastDate),HttpStatus.OK);
    }


    @GetMapping(value = "/sum-total-price/{startDate}/{lastDate}")
    public ResponseEntity<Long> sumTotalPriceBetween(@PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @PathVariable("lastDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate )
    {
        Long result = cartService.sumTotalPriceBetwenn(startDate,lastDate);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }







}
