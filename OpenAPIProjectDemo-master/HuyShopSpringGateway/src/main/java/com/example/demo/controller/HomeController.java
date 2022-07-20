package com.example.demo.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class HomeController {


    @GetMapping("/index")
    @RolesAllowed("admin")
    public String index()
    {
        return "hello";
    }

}
