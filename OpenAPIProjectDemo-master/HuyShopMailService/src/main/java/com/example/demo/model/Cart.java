package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class Cart {


    private String oderNumber;
    private Double totalPrice;
    private String status;
    private String shippingAddress;
    private String userNameOrder;
    private String email;
    private List<CartItem> cartItemDTOList;
    private Boolean isSending;

}
