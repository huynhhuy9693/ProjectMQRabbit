package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface MailServiceFeignClientUser {

//    @PostMapping("/admin-user/user")
//    User createUser(@RequestBody User user);
    @PutMapping(value = "/admin-user/user/{id}")
    User editUser(@PathVariable Long id,@RequestBody User user);
    @GetMapping(value = "/admin-user/user/{username}")
    User findByUserName(@PathVariable("username") String userName);


}