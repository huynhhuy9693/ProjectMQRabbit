package com.example.demo.service;

import com.example.demo.controller.CartAuthentication;
import com.example.demo.dto.*;
import com.example.demo.entity.CartEntity;
import com.example.demo.entity.CartItemEntity;
import com.example.demo.model.Cart;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
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

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    PaymentFeignClient paymentFeignClient;

    @Autowired
    ReportFeignClient reportFeignClient;

    @Autowired
    CartAuthentication cartAuthentication;

    @Autowired
    StreamBridge streamBridge;

    private Logger logger = LoggerFactory.getLogger(CartService.class);

    public Cart findByOrderNumber(String oderNumber)
    {
        CartEntity cart = repository.findByOderNumber(oderNumber);
        Cart cartDTO = modelMapper.map(cart, Cart.class);
        return cartDTO;
    }

    @Transactional
    public Integer updateStatusByOrdernumber(String status , String orderNumber)
    {
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
        List<Cart> cartList =  cartEntityList.stream().map(item->modelMapper.map(item,Cart.class)).collect(Collectors.toList());
        return cartList;
    }

    public List<Cart> findByIsSending()
    {
        List<CartEntity> cartEntityList = repository.findByIsSendingFalse();
        List<Cart> cartList  = cartEntityList.stream().map(item->modelMapper.map(item,Cart.class)).collect(Collectors.toList());
        return cartList;
    }
    public List<Cart> findByDateOrderBetween(LocalDate startDate, LocalDate lastDate,String token )
    {
        List<CartEntity> cartEntityList = repository.findByDateOrderBetween(startDate,lastDate);
        List<Cart> cartList = cartEntityList.stream().map(item->modelMapper.map(item,Cart.class)).collect(Collectors.toList());
        return cartList;
    }

    public Long sumTotalPriceBetween(LocalDate startDate, LocalDate lastDate,String token)
    {
        Long result = repository.sumByDateOrderBetween(startDate,lastDate);
        return result;
    }


    @Transactional
    public void deliveryAndUpdate(String orderNumber)

    {
        List<CartItemEntity> cartItemEntityList =cartItemRepository.findByOrdernumber(orderNumber);
        CartEntity cart = repository.findByOderNumber(orderNumber);
        List<CartItemDTO> cartItemDTOLis = cartItemEntityList.stream().map(item->modelMapper.map(item, CartItemDTO.class)).collect(Collectors.toList());

        if(cart.getStatus().equalsIgnoreCase("ORDER-SUCCESS"))
        {
            for(CartItemDTO cartItemDTO: cartItemDTOLis)
            {
                int quantityDelivery = productFeignClient.getDeliveryById(cartItemDTO.getProductId());
                int quantityCartItem = cartItemDTO.getQuantity();
                int result = quantityDelivery+quantityCartItem;
                productFeignClient.updateDeliveryForId(result, cartItemDTO.getProductId());
            }
            logger.debug("status update :" +cart.getStatus());
            cart.setStatus("DELIVERED");
            repository.save(cart);
        }else
        {
            logger.debug("status update :" +cart.getStatus());
            cart.setStatus("SUCCESS");
            repository.save(cart);
        }


    }

    public void delete(Long id,String orderNumber)
    {
        CartEntity cart = repository.findByOderNumber(orderNumber);
        cart.setStatus("DELETE");
        repository.deleteById(id);
    }

    public CartDTO findById(Long id)
    {
        Optional<CartEntity> cartEntity = repository.findById(id);
        CartDTO cart = modelMapper.map(cartEntity,CartDTO.class);
        return cart;

    }

}
