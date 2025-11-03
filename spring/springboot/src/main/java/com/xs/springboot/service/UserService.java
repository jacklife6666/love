package com.xs.springboot.service;

import com.xs.springboot.dao.IUserDao;
import com.xs.springboot.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService  implements IUserService{
    @Autowired
    IUserDao userDao;
    @Override
    public void getUser() {
        userDao.getUser();
    }

}
