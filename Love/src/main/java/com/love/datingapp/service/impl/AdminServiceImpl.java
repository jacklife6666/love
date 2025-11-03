package com.love.datingapp.service.impl;

import com.love.datingapp.common.dto.UserVO;
import com.love.datingapp.entity.User;
import com.love.datingapp.mapper.UserMapper;
import com.love.datingapp.service.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    // 【新增】注入密码编码器，用于比对密码
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserVO> getAllUsers() {
        return userMapper.findAllUsers();
    }

    /**
     * 【新增】验证管理员密码是否正确的逻辑
     * @param adminId     管理员ID
     * @param rawPassword 前端传来的明文密码
     * @return 密码是否匹配
     */
    @Override
    public boolean verifyAdminPassword(Long adminId, String rawPassword) {
        User admin = userMapper.findById(adminId);
        if (admin == null) {
            return false;
        }
        // 使用 Spring Security 的工具来比对明文和加密后的密码
        return passwordEncoder.matches(rawPassword, admin.getPassword());
    }
}