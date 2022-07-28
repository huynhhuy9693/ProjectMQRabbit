package com.example.demo.service;


import com.example.demo.model.JwtRequest;
import com.example.demo.model.UserOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserFeignClient {


    @GetMapping(value = "/admin-user/user/all")
    List<JwtRequest> findAll();

    @GetMapping(value = "/admin-user/user/{username}")
    UserOrder findByUserName(@PathVariable("username") String userName);
}
