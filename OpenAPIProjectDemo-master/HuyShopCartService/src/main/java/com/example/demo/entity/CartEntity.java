package com.example.demo.entity;


import com.example.demo.dto.UserOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cartEntity")
    private List<CartItemEntity> cartItemEntityList = new LinkedList<>();

    public void add(CartItemEntity item) {

        if (item != null) {
            if(cartItemEntityList == null) {
                cartItemEntityList = new LinkedList<>();
            }
        }
        cartItemEntityList.add(item);
        item.setCartEntity(this);
    }



}
