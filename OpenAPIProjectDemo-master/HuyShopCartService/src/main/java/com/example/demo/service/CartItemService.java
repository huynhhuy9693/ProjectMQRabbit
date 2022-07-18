package com.example.demo.service;


import com.example.demo.dto.CartItemDTO;
import com.example.demo.entity.CartItemEntity;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    @Autowired
    CartItemRepository repository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductFeignClient productFeignClient;

    Map<Long, CartItemEntity>  maps = new HashMap<>();

   public Set<CartItem> findById(Long id)
   {

            Set<CartItem> cartItems = new HashSet<>();
           for(CartItemEntity request : repository.findAll())
           {
               if(request.getId()==id)
               {
                   CartItem response = modelMapper.map(request, CartItem.class);
                   cartItems.add(response);
               }
               return cartItems;
           }
           return null;
       }

    public void addCartItem(CartItemEntity item)
    {

        CartItemEntity cartItemEntity = maps.get(item.getProductId());
        if(cartItemEntity==null)
        {
            maps.put(item.getProductId(), item);
        }else
        {
            cartItemEntity.setQuantity(cartItemEntity.getQuantity()+1);
        }
    }

    public void removeCartItem(Long id)
    {
        maps.remove(id);
    }
    public CartItemEntity updateQuantity(Long productId)
    {
        CartItemEntity cartItem = maps.get(productId);
        if(cartItem.getQuantity()<=productFeignClient.getQuantityById(productId))
        {
            cartItem.setQuantity(cartItem.getQuantity());
            return cartItem;
        }
        removeCartItem(productId);
        return null;
    }
    public void clearCartItem()
    {
        maps.clear();
    }
    public Collection<CartItemEntity> getAllCartItem()
    {
        return maps.values();
    }
    public int getTotalQuantity()
    {
        return maps.values().size();
    }
    public double getTotalPrice()
    {
        return maps.values().stream().mapToDouble(item->item.getPrice()*item.getQuantity()).sum();
    }

    public Integer countCartItemByProductId(Long productId) {

        int result = repository.countCartItemByProductId(productId);
        return result;
    }

    public List<CartItem> findByOrdernumber(String oderNumber)
    {
        List<CartItemEntity> cartItemEntityList = repository.findByOrdernumber(oderNumber);
        List<CartItem> cartItemList = cartItemEntityList.stream().map(item->modelMapper.map(item , CartItem.class)).collect(Collectors.toList());
        return cartItemList;
    }
    public List<CartItem> findByOrdernumberAndStatus(String oderNumber, String status)
    {
        List<CartItemEntity> cartItemEntityList = repository.findByOrdernumberAndStatus(oderNumber,"SUCCESS");
        List<CartItem> cartItemList = cartItemEntityList.stream().map(item->modelMapper.map(item , CartItem.class)).collect(Collectors.toList());
        return cartItemList;
    }
}

