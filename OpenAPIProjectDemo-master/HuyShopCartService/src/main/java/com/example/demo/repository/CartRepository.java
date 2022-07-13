package com.example.demo.repository;

import com.example.demo.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface
CartRepository extends JpaRepository<CartEntity,Long> {

    CartEntity findByOderNumber(String oderNumber);

    CartEntity findByUserNameOrder(String userNameOrder);

    @Modifying
    @Query("Update CartEntity c set c.status=:status where c.oderNumber=:orderNumber")
    Integer updateStatusByOrdernumber(@Param("status") String status,@Param("orderNumber") String orderNumber);


    @Query("SELECT SUM(c.totalPrice) FROM CartEntity c")
    Long sumTotalPrice();

    List<CartEntity> findByDateOrder(LocalDate dateOrder);

    List<CartEntity> findByIsSendingFalse();

    @Modifying
    @Query("Update CartEntity c set c.isSending=:isSending where c.oderNumber=:orderNumber")
    Integer updateIsSendingTrue(@Param("isSending") Boolean isSending,@Param("orderNumber") String orderNumber);


}
