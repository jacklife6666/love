package com.love.datingapp.service.impl;

import com.love.datingapp.common.dto.UserVO;
import java.util.List;

public interface AdminService {
    List<UserVO> getAllUsers();

    boolean verifyAdminPassword(Long adminId, String rawPassword);
}