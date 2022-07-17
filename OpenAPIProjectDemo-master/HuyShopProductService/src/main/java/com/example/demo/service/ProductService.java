package com.example.demo.service;


import com.example.demo.entity.ProductEntity;
import com.example.demo.model.Product;

import com.example.demo.repository.ProductRepository;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.transaction.Transactional;
import java.io.*;
import com.cloudinary.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 100
)
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CartItemFeignClient cartItemFeignClient;

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "huyhuynh",
            "api_key", "186243663589179",
            "api_secret", "xIhyZNk6NYFIuMSHRhZP0srJL_k",
            "secure", true));

    private static  final String SAVE_PATH = "D:\\image";

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

    public Product save(Product product) throws IOException {
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

    public String encodeImage(String image, String savePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(image);
        byte[] data = fileInputStream.readAllBytes();
        String img_url =Base64.getEncoder().encodeToString(data);
        FileWriter fileWriter = new FileWriter(savePath);
        fileWriter.write(img_url);
        fileWriter.close();
        fileInputStream.close();
        return img_url;
    }

    public void decodeImage(String txtPath, String savePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(txtPath);
        fileInputStream.readAllBytes();
        byte[] data=Base64.getDecoder().decode(new String((fileInputStream.readAllBytes())));
        FileOutputStream fileOutputStream= new FileOutputStream(savePath);
        fileOutputStream.write(data);
        fileOutputStream.close();
        fileInputStream.close();

    }


}
