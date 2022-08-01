package com.example.demo.repository;

import com.example.demo.sync.CartDataSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartDataSync,Long>{

    @Query("SELECT c FROM CartDataSync c WHERE c.dateOrder BETWEEN :startDay AND :lastDay ORDER BY c.dateOrder")
    List<CartDataSync> findByDateOrderBetween(@Param("startDay") LocalDate startDay , @Param("lastDay") LocalDate lastDay);

    @Query("SELECT SUM(c.totalPrice) FROM CartDataSync c WHERE c.dateOrder BETWEEN :startDay AND :lastDay ")
    Long  sumByDateOrderBetween(@Param("startDay") LocalDate startDay , @Param("lastDay") LocalDate lastDay);

}
