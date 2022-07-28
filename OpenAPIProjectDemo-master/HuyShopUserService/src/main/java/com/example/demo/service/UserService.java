package com.example.demo.service;


import com.example.demo.aspect.UserAOP;
import com.example.demo.entity.UserEntity;
import com.example.demo.model.User;
import com.example.demo.reactive.UserBinding;
import com.example.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableBinding(UserBinding.class)
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(UserService.class);


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


    public User findByUserId(Long id)
    {
        Optional<UserEntity> request = repository.findById(id);
        User reponse = modelMapper.map(request, User.class);
        return reponse;

    }

    public List<User> findALlByUserNameAndPassword() {
        try {
            List<UserEntity> entityList = repository.findAllUserNameAndPassword();
            List<User> userList = entityList.stream().map(item -> modelMapper.map(item, User.class)).collect(Collectors.toList());
            return userList;
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }
            return null;
    }

    public User findPassWordById(Long id)
    {
        UserEntity request = repository.findPassWordById(id);
        User response = modelMapper.map(request, User.class);
        return response;
    }

    @StreamListener(target = UserBinding.SEND_MESS)
    public void receiveMess(User user)
    {
        logger.info("send mail success for user " + user.getUserName());
    }

}
