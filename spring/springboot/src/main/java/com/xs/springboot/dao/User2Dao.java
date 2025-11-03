package com.xs.springboot.dao;

public class User2Dao implements IUserDao{
    @Override
    public void getUser() {
        System.out.println("hello spring2");
    }
}
