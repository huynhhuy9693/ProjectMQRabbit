package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseDTO {

    User userOrder;
    Cart cartEntity;
    String Status;
}
