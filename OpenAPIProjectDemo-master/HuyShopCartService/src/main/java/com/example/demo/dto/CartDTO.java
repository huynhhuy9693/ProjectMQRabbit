package com.example.demo.dto;

import com.example.demo.entity.CartItemEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private Long id ;
    private String oderNumber;
    private Double totalPrice;
    private String status;
    private List<CartItemDTO> cartItemDTOList = new LinkedList<>();
    private UserOrder userOrder;
    private String shippingAddress;
    private String userNameOrder;
    private String email;
    private LocalDate dateOrder;
    private Boolean isSending;

//    public void add(CartItemDTO item) {
//
//        if (item != null) {
//            if(cartItemDTOList == null) {
//                cartItemDTOList = new LinkedList<>();
//            }
//        }
//        item.setCartDTO(this);
//    }
}
