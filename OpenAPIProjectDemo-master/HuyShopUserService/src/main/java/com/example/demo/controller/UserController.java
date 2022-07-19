package com.example.demo.controller;

import com.example.demo.api.UserApi;
import com.example.demo.entity.UserEntity;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/admin-user")
public class UserController implements UserApi {
    @Autowired
    private UserService service;

    @Override
    public ResponseEntity<List<User>> getAllUser()
    {
        return new ResponseEntity<>(service.findAll(),HttpStatus.OK);
    }
//    @Override
//    public ResponseEntity<User> findByUserId(
//            @Parameter(name = "id", description = "ID of user to return", required = true)
//            @PathVariable("id") Long id
//    )
//    {
//        return new ResponseEntity<>(service.findById(id),HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<User> createUser(
            @Parameter(name = "User", description = "create new user", required = true) @Valid @RequestBody User user
    ) {

        return new ResponseEntity<>(service.save(user),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<User> updateUser(
            @Parameter(name = "id", description = "ID of user to return", required = true) @PathVariable("id") Long id,
            @Parameter(name = "User", description = "update user", required = true) @Valid @RequestBody User user
    ) {
        return new ResponseEntity<>(service.save(user),HttpStatus.OK);
    }

    @Override

    public ResponseEntity<Void> deleteUser(
            @Parameter(name = "id", description = "ID of user to return", required = true) @PathVariable("id") Long id
    ) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/user/{username}")
    @RolesAllowed("admin")
    public ResponseEntity<User> findByUser(@PathVariable ("username") String userName)
    {
        User user = service.findByUserName(userName);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    @RolesAllowed("user")
    public  ResponseEntity<UserEntity> findByUsersId(@PathVariable ("id") Long id)
    {
        UserEntity user = service.findByUserId(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping(value = "/search/all")
    public ResponseEntity<List<User>> findALlByUserNameAndPassword()
    {
        return new ResponseEntity<>(service.findALlByUserNameAndPassword(), HttpStatus.OK);
    }
}
