package com.example.demo.service;

import com.example.demo.model.CartDTO;
import com.example.demo.repository.CartRepository;
import com.example.demo.sync.CartDataSync;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    ModelMapper modelMapper;
    public CartDataSync save(CartDataSync cartDataSync)
    {
        return cartRepository.save(cartDataSync);
    }

    public List<CartDTO> findByDateOrderBetween(LocalDate startDate, LocalDate lastDate)
    {
        List<CartDataSync> cartEntityList = cartRepository.findByDateOrderBetween(startDate,lastDate);
        List<CartDTO> cartList = cartEntityList.stream().map(item->modelMapper.map(item,CartDTO.class)).collect(Collectors.toList());
        return cartList;
    }

    public Long sumTotalPriceBetween(LocalDate startDate, LocalDate lastDate)
    {
        Long result = cartRepository.sumByDateOrderBetween(startDate,lastDate);
        return result;
    }

    public void delete(Long id)
    {
        cartRepository.deleteById(id);
    }
}
