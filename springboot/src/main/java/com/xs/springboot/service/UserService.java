package com.xs.springboot.service;

import com.xs.springboot.mapper.UserMapper;
import com.xs.springboot.pojo.DTO.UserDTO;
import com.xs.springboot.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public void add(UserDTO user) {


    User user1=new User();
            BeanUtils.copyProperties(user,user1);
            userMapper.save(user1);
        }
    }
