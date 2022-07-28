package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long id ;
    private String oderNumber;
    private Double totalPrice;
    private String status;
    private LocalDate dateOrder;
    private String shippingAddress;
    private String  userOrder;
    private String email;
    private Boolean isSending;
    private String voucher;
    private String payment;

}
