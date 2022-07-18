package com.example.demo.service;


import com.example.demo.model.ProductReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name="product-service")
public interface ProductReportService {
    @GetMapping("/admin-product/product/all")
    List<ProductReport> getProductData();

    @GetMapping(value = "/admin-product/product/{id}")
    ProductReport findById(@PathVariable ("id") Long id);
}
