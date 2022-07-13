package com.example.demo.service;


import com.example.demo.dto.UserOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping(value = "/admin-product/quantity/{id}")
    Integer getQuantityById(@PathVariable Long id);

    @PutMapping(value = "/admin-product/product/{id}/{quantity-present}")
    Integer updateProductQuantityForId(@PathVariable ("quantity-present") int quantityPresent,@PathVariable ("id") Long id);

    

}
