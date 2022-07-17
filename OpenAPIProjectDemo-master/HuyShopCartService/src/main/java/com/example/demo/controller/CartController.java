package com.example.demo.controller;


import com.example.demo.dto.CartDTO;
import com.example.demo.dto.Purchase;
import com.example.demo.dto.PurchaseResponse;
import com.example.demo.facade.PurchaseFacade;
import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    PurchaseFacade purchaseFacade;

    @PostMapping(value = "/purchase")
    public ResponseEntity<PurchaseResponse> placeOrder(@RequestBody  Purchase purchase) throws MalformedURLException, MessagingException, UnsupportedEncodingException, JsonProcessingException {
        PurchaseResponse purchaseResponse = purchaseFacade.placeOrder(purchase);
        return new ResponseEntity<>(purchaseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/{orderNumber}")
    public ResponseEntity<CartDTO> findByOrderNumber(@PathVariable ("orderNumber") String orderNumber)
    {
        return new ResponseEntity<>(cartService.findByOrderNumber(orderNumber), HttpStatus.OK);
    }

    //update status in cart DB
    @PutMapping(value = "/{status}/{orderNumber}")
    public ResponseEntity<Integer> updateStatusByOrdernumber(@PathVariable("status") String status, @PathVariable("orderNumber") String orderNumber)
    {
       int result = cartService.updateStatusByOrdernumber(status,orderNumber);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    //update is-sending in cart DB
    @PutMapping(value = "/is-sending/{isSending}/{orderNumber}")
    public ResponseEntity<Integer> updateIsSendingTrue(@PathVariable("isSending") Boolean isSending, @PathVariable("orderNumber") String orderNumber)
    {
        int result = cartService.updateIsSendingByOrderNumber(true,orderNumber);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
