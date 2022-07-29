package com.example.demo.sync;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_data_sync")
public class CartDataSync {

    @Id
    @Column(name = "id")
    private Long id ;
    @Column(name = "oder_number")
    private String oderNumber;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "status")
    private String status;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "date_order")
    private LocalDate dateOrder;
    private String shippingAddress;
    private String userNameOrder;
    private String email;
    private Boolean isSending;
    private String voucher;
    private String payment;
}
