package com.love.datingapp.controller;

import com.love.datingapp.common.dto.LoginDTO;
import com.love.datingapp.common.dto.LoginVO;
import com.love.datingapp.common.dto.RegisterDTO;
import com.love.datingapp.service.impl.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 添加了构造函数和打印语句
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
        System.out.println("==============================================");
        System.out.println("AuthController Bean has been created!");
        System.out.println("==============================================");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return ResponseEntity.ok("注册成功");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return ResponseEntity.ok(loginVO);
    }
}