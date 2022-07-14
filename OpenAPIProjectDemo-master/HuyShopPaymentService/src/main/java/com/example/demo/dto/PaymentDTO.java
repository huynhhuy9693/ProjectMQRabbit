package com.example.demo.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO implements Serializable {
    private int id;
    private String name;
}
