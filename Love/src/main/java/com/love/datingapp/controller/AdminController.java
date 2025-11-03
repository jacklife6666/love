package com.love.datingapp.controller;

import com.love.datingapp.common.dto.UserVO;
import com.love.datingapp.service.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserVO>> getAllUsers() {
        List<UserVO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 【新增】验证管理员密码的接口
     */
    @PostMapping("/verify-password")
    public ResponseEntity<Void> verifyAdminPassword(@AuthenticationPrincipal Long currentAdminId, @RequestBody Map<String, String> payload) {
        String password = payload.get("password");
        boolean isCorrect = adminService.verifyAdminPassword(currentAdminId, password);
        if (isCorrect) {
            return ResponseEntity.ok().build();
        } else {
            // 返回 401 表示未授权或密码错误
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 【新增】获取指定用户密码的接口
     * 注意：在真实生产项目中，直接返回任何形式的密码都是极不安全的。
     * 这里的实现仅为满足当前需求，返回的是用户加密后的密码。
     */
    @GetMapping("/user-password/{userId}")
    public ResponseEntity<Map<String, String>> getUserPassword(@PathVariable Long userId) {
        // 为了安全，我们不返回明文密码，而是返回数据库中已加密的密码字符串
        // 让管理员知道这个用户的密码“存在”，但看不到原文
        UserVO user = adminService.getAllUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (user != null) {
            return ResponseEntity.ok(Map.of("password", user.getPassword()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}