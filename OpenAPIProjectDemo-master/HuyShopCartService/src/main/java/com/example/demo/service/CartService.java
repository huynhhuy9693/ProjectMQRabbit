package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.CartEntity;
import com.example.demo.entity.CartItemEntity;
import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

   @Autowired
   private ProductFeignClient productFeignClient;
    @Autowired
    CartRepository repository;

    @Autowired
    MailFeignClient mailFeignClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemService cartItemService;

    public CartDTO findByOrderNumber(String oderNumber)
    {
        CartEntity cart = repository.findByOderNumber(oderNumber);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        return cartDTO;
    }

    @Transactional
    public Integer updateStatusByOrdernumber(String status , String orderNumber)
    {
        status ="SUCCESS";
        int result = repository.updateStatusByOrdernumber(status,orderNumber);
        return result;
    }

    @Transactional
    public Integer updateIsSendingByOrderNumber(Boolean isSending , String orderNumber)
    {
        int result = repository.updateIsSendingTrue(true,orderNumber);
        return result;
    }



    public List<Cart> findAll()
    {
        List<CartEntity> request = repository.findAll();
        List<Cart> cartList = request.stream().map(item->modelMapper.map(item, Cart.class)).collect(Collectors.toList());
        return cartList;

    }

    public Long sumTotalPrice()
    {
        Long result = repository.sumTotalPrice();
        return result;
    }

    public List<Cart> findByOrderDate(LocalDate orderDate)
    {
        List<CartEntity> cartEntityList = repository.findByDateOrder(orderDate);
        List<Cart> cartList = new ArrayList<>();
        for (CartEntity c:cartEntityList)
        {
            Cart cart = modelMapper.map(c, Cart.class);
            cartList.add(cart);
        }
        return cartList;
    }

    public List<Cart> findByIsSending()
    {
        List<CartEntity> cartEntityList = repository.findByIsSendingFalse();
        List<Cart> cartList = new ArrayList<>();
        for (CartEntity c: cartEntityList) {
            Cart cart = modelMapper.map(c, Cart.class);
            cartList.add(cart);
        }
        return cartList;
    }


}
