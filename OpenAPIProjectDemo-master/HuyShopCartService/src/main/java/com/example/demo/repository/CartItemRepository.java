package com.example.demo.repository;

import com.example.demo.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity,Long> {

    @Query("select c from CartItemEntity c where c.cartEntity.oderNumber =:oderNumber")
    List<CartItemEntity> findByOrdernumber(@Param("oderNumber") String  oderNumber);

    @Query("SELECT SUM(c.quantity) FROM CartItemEntity c WHERE c.productId=:productId")
    Integer countCartItemByProductId(@Param("productId") Long productId);

    @Query("select c from CartItemEntity c where c.cartEntity.oderNumber =:oderNumber AND c.cartEntity.status=:status")
    List<CartItemEntity> findByOrdernumberAndStatus(@Param("oderNumber") String  oderNumber, @Param("status") String status);

}
