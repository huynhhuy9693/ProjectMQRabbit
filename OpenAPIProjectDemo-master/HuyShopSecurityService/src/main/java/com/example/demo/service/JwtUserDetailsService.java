package com.example.demo.service;

import com.example.demo.model.JwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserFeignClient userFeignClient;
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (JwtRequest j : userFeignClient.findAll()) {

            if (j.getUserName().equalsIgnoreCase(username))
            {
                return new User(j.getUserName(),passwordEncoder().encode(j.getPassWord()),
                        new ArrayList<>());
            }
        }
        return null;

    }
}
