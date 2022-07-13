package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cart {


    private String oderNumber;
    private Double totalPrice;
    private String status;
    private UserOrder userOrder;
    private String shippingAddress;
    private String userNameOrder;
    private String email;
    private List<CartItem> cartItemDTOList;
    private Boolean isSending;

}
