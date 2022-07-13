package com.example.demo.service;


import com.example.demo.dto.UserOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping(value = "/admin-user/user/{username}")
    UserOrder findByUserName(@PathVariable("username") String userName);
}
