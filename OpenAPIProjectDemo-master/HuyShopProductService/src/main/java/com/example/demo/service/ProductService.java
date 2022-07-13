package com.example.demo.service;

import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.ProductEntity;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import io.swagger.models.auth.In;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CartItemFeignClient cartItemFeignClient;


    public List<Product> findAll()
    {
        List<ProductEntity> request = repository.findAll();
        List<Product> productList = request.stream().map(item->modelMapper.map(item, Product.class)).collect(Collectors.toList());
        return productList;
    }

    public Product findById(Long id)
    {
        for(ProductEntity request : repository.findAll())
        {
            if(request.getId()==id)
            {
                Product response = modelMapper.map(request, Product.class);
                return response;
            }
        }
        return null;
    }

    public Product save(Product product)
    {
        ProductEntity request = modelMapper.map(product, ProductEntity.class);
        ProductEntity productEntity = repository.save(request);
        Product response = modelMapper.map(productEntity , Product.class);
        return response;

    }
    public void delete(Long id)
    {
        repository.deleteById(id);
    }

    public List<Product> findByName(String name)
    {
        List<ProductEntity> request = repository.findByName(name);
        List<Product> response = request.stream().map(item->modelMapper.map(item, Product.class)).collect(Collectors.toList());
        return response;
    }

    public Integer getQuantityById(Long id)
    {
        int quantity = repository.getQuantityById(id);
       return quantity;
    }

    @Transactional
    public Integer updateProductQuantityForId(int quantityPresent,Long id)
    {
        int result = repository.updateProductQuantityForId(quantityPresent,id);
        return result;
    }
}
