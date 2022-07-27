package com.example.demo.controller;

import com.example.demo.api.ProductApi;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.ProductEntity;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping(value = "/admin-product")
public class ProductController implements ProductApi {
    @Autowired
    private ProductService service;


    @Override
    public ResponseEntity<List<Product>> getAllProduct()
    {
        return new ResponseEntity<>(service.findAll(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Product> findByProductId(
            @Parameter(name = "id", description = "ID of product to return", required = true)
            @PathVariable("id") Long id
    )
    {

        return new ResponseEntity<>(service.findById(id),HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<Product> createProduct(
//            @Parameter(name = "Product", description = "create new product", required = true)
//            @Valid @RequestBody Product product
//    ) {
//        if(service.save(product)==null)
//        {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//
//    }

    @PostMapping(value = "/product/save-product")

        public ResponseEntity<Product> saveProduct(@ModelAttribute Product product, HttpServletRequest request)  {

        Product response = service.save(product, request);
        if(response==null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }

//    @Override
//    public ResponseEntity<Product> updateProduct(
//            @Parameter(name = "id", description = "ID of category to return", required = true) @PathVariable("id") Long id,
//            @Parameter(name = "Product", description = "update product", required = true) @Valid @RequestBody Product product
//    ) {
//        service.findById(id);
//        return new ResponseEntity<>(service.save(product),HttpStatus.OK);
//    }

    @Override

    public ResponseEntity<Void> deleteProduct(
            @Parameter(name = "id", description = "ID of product to return", required = true)
            @PathVariable("id") Long id
    ) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/product/name/{name}")
    public ResponseEntity<Product> findByName(@PathVariable ("name") String name)
    {
        Product product = service.findByName(name);
        return new ResponseEntity<>(product,HttpStatus.OK);

    }

    @GetMapping(value = "/quantity/{id}")
    public ResponseEntity getQuantityById(@PathVariable  Long id)
    {
        int quantity = service.getQuantityById(id);
       return new ResponseEntity<>(quantity,HttpStatus.OK);
    }

    @PutMapping(value = "/product/{id}/{quantity-present}")
    public ResponseEntity<Integer> updateProductQuantityForId(@PathVariable("quantity-present") int quantityPresent,@PathVariable("id") Long id)
    {
        int result = service.updateProductQuantityForId(quantityPresent ,id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping(value = "/delivery/{id}")
    public ResponseEntity getDeliveryById(@PathVariable  Long id)
    {
        int quantity = service.getDeliveryById(id);
        return new ResponseEntity<>(quantity,HttpStatus.OK);
    }

    @PutMapping(value = "/product/delivery/{id}/{delivery}")
    public ResponseEntity<Integer> updateDeliveryForId(@PathVariable("delivery") int delivery,@PathVariable("id") Long id)
    {
        int result = service.updateDeliveryForId(delivery ,id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }





}
