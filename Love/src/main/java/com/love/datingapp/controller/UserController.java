package com.love.datingapp.controller;

import com.love.datingapp.entity.UserProfile;
import com.love.datingapp.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/recommendations")
    public ResponseEntity<List<UserProfile>> getRecommendations(
            @AuthenticationPrincipal Long currentUserId,
            @RequestParam(defaultValue = "10") int limit) {

        System.out.println("获取到的当前用户ID是: " + currentUserId);

        // 恢复对 Service 的正常调用
        List<UserProfile> recommendations = userService.getRecommendations(currentUserId, limit);
        return ResponseEntity.ok(recommendations);
    }
}