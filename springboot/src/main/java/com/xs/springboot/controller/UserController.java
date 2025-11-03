package com.xs.springboot.controller;

import com.xs.springboot.pojo.DTO.UserDTO;
import com.xs.springboot.pojo.ResponseMessage;

import com.xs.springboot.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//接口对象返回对象，转换成json文本
@RequestMapping("/user")//接口路径，访问接口路径为：http://localhost:8080/user
public class UserController {
    //增加
    @Autowired
    IUserService userService;
     @PostMapping//URL 为：http://localhost:8080/user method：POST
      public ResponseMessage add(@RequestBody UserDTO user){
         userService.add(user);

return ResponseMessage.success(null);
     }
}
