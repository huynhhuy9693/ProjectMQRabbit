package com.example.demo.controller;

import com.example.demo.api.ApiUtil;
//import com.example.demo.api.ProductApi;
import com.example.demo.api.CategoryApi;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller

@RequestMapping(value = "/admin-product", produces = {MediaType.APPLICATION_JSON_VALUE})

public class CategoryController implements CategoryApi {

    @Autowired
    private CategoryService service;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<List<Category>> getAllCategory()
    {
       return new ResponseEntity<>(service.findAll(),HttpStatus.OK);
    }
    @Override
    public ResponseEntity<Category> findById(
            @Parameter(name = "id", description = "ID of category to return", required = true)
            @PathVariable("id") Long id)
    {
        return new ResponseEntity<>(service.findById(id),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Category> createCategory(
            @Parameter(name = "Category", description = "create new category", required = true)
            @Valid @RequestBody Category category)
    {
        if(service.save(category)==null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(service.save(category),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Category> updateCategory(
            @Parameter(name = "id", description = "ID of category to return", required = true) @PathVariable("id") Long id,
            @Parameter(name = "Category", description = "update category", required = true) @Valid @RequestBody Category category)
    {
        return new ResponseEntity<>(service.save(category),HttpStatus.OK);
    }

    @Override

    public ResponseEntity<Void> deleteCategory(
            @Parameter(name = "id", description = "ID of category to return", required = true) @PathVariable("id") Long id
    ) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }






}
