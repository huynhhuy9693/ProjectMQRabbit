package com.example.demo.repository;

import com.example.demo.entity.ProductEntity;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {

    ProductEntity findByName(String name);

    @Query("select p.quantityPresent from ProductEntity p where p.id = :id")
    Integer getQuantityById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update ProductEntity p set p.quantityPresent=:quantityPresent where p.id=:id")
    Integer updateProductQuantityForId(@Param("quantityPresent") int quantityPresent, @Param("id") Long id);

    @Query("select p.delivery from ProductEntity p where p.id = :id")
    Integer getQuantityDeliveryById(@Param("id") Long id);


    @Modifying
    @Query("update ProductEntity p set p.delivery=:delivery where p.id=:id")
    Integer updateDelieveryForId(@Param("delivery") int delivery, @Param("id") Long id);
}