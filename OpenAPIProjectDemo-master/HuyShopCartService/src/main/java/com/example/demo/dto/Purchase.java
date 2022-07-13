package com.example.demo.dto;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.CartItemEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class Purchase {

    UserOrder userOrder;
    CartDTO cartDTO;
//    List<CartItemDTO> cartItemDTOList;
    String shippingAddress;
    String status;

}
