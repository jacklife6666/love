package com.xs.springboot.service;

import com.xs.springboot.pojo.DTO.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    /*插入用户*/
    void add(UserDTO user);
}
