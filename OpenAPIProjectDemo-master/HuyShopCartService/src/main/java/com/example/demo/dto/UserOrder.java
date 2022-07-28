package com.example.demo.dto;


import com.example.demo.entity.CartEntity;
import com.example.demo.model.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter


public class UserOrder {

    private Long id;
    private String userName;
    private String passWord;
    private String address;
    private String phone;
    private  String email;
    private String name;


}
