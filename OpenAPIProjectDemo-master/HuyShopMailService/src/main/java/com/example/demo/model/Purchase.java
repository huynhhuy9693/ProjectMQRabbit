package com.example.demo.model;

import lombok.*;

import java.util.List;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Purchase {


    User userOrder;
    Cart cartDTO;
    String shippingAddress;
    String status;


}
