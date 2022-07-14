package com.example.demo.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO implements Serializable {
    private int id;
    private String name;
}
