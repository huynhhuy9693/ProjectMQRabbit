package com.example.demo.controller;


import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/cart")
public class CartItemController {

    @Autowired
    CartItemService service;

    @GetMapping(value = "/cart-item/{id}")
    public ResponseEntity <Set<CartItem>> findById(@PathVariable("id") Long id)
    {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/cart-item/product-id/{productId}")
    public ResponseEntity<Integer> countCartItemByProductId(@PathVariable ("productId") Long productId)
    {
        int result = service.countCartItemByProductId(productId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping(value = "/cart-item/cart/{oderNumber}")
    public ResponseEntity<List<CartItem>> findByOrderNumber(@PathVariable ("oderNumber") String oderNumber)
    {
        List<CartItem> cartItemList = service.findByOrdernumber(oderNumber);
        return new ResponseEntity<>(cartItemList,HttpStatus.OK);
    }
}
