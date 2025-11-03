package com.love.datingapp.service.impl;

import com.love.datingapp.common.dto.LoginDTO;
import com.love.datingapp.common.dto.LoginVO;
import com.love.datingapp.common.dto.RegisterDTO;

public interface AuthService {
    void register(RegisterDTO registerDTO);
    LoginVO login(LoginDTO loginDTO);
}