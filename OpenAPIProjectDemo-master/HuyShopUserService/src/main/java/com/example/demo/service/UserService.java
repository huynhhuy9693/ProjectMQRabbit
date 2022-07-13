package com.example.demo.service;


import com.example.demo.entity.UserEntity;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    public List<User> findAll()
    {
        List<UserEntity> request = repository.findAll();
        List<User> userList = request.stream().map(item->modelMapper.map(item, User.class)).collect(Collectors.toList());
        return userList;
    }

    public User findById(Long id)
    {
        for(UserEntity request : repository.findAll())
        {
            if(request.getId()==id)
            {
                User response = modelMapper.map(request, User.class);
                return response;
            }
        }
        return null;
    }

    public User save(User user)
    {
        UserEntity request = modelMapper.map(user, UserEntity.class);
        UserEntity userEntity = repository.save(request);
        User response = modelMapper.map(userEntity , User.class);
        rabbitTemplate.convertAndSend(exchange,routingkey,response);
        return response;
    }
    public void delete(Long id)
    {
        repository.deleteById(id);
    }

    public User findByUserName(String userName)
    {
        UserEntity userEntity = repository.findByUserName(userName);
        User user = modelMapper.map(userEntity, User.class);
        return user;

    }


    public UserEntity findByUserId(Long id)
    {
        for(UserEntity request : repository.findAll())
        {
            if(request.getId()==id)
            {
               return request;
            }
        }
        return null;
    }
}
