package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import com.example.demo.model.User;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUserName(String userName);

    @Query("SELECT u.userName,u.passWord from UserEntity u ")
    List<UserEntity> findAllUserNameAndPassword();
}


