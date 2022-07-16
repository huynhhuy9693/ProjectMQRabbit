package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartReport {

    private Long id ;
    private String oderNumber;
    private Double totalPrice;
    private String status;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOrder;
    private String shippingAddress;
    private String userOrder;
    private String email;
    private Boolean isSending;
    private String voucher;
    private String payment;

}
