package com.example.demo.service;

import com.example.demo.repository.CartRepository;
import com.example.demo.sync.CartDataSync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;
    public CartDataSync save(CartDataSync cartDataSync)
    {
        return cartRepository.save(cartDataSync);
    }
}
