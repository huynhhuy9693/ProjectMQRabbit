package com.example.demo.controller;


import com.example.demo.api.CartApi;
import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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







}
