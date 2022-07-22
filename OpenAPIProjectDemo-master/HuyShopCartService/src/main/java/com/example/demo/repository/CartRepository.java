package com.example.demo.repository;

import com.example.demo.entity.CartEntity;
import com.example.demo.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;

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


    @Query("SELECT c FROM CartEntity c WHERE c.dateOrder BETWEEN :startDay AND :lastDay ORDER BY c.dateOrder")
    List<CartEntity> findByDateOrderBetween(@Param("startDay") LocalDate startDay , @Param("lastDay") LocalDate lastDay);

    @Query("SELECT SUM(c.totalPrice) FROM CartEntity c WHERE c.dateOrder BETWEEN :startDay AND :lastDay ")
    Long  sumByDateOrderBetween(@Param("startDay") LocalDate startDay , @Param("lastDay") LocalDate lastDay);
}
