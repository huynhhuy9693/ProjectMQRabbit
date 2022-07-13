package com.example.demo.model;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    private Long id;
    private String userName;
    private String passWord;
    private String email;
    private String name;
    private String address;
    private String phone;
    private LocalDate dob;

}
