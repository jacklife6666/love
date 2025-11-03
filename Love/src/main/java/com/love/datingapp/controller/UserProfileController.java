package com.love.datingapp.controller;

import com.love.datingapp.common.dto.UserProfileDTO;
import com.love.datingapp.entity.UserProfile;
import com.love.datingapp.service.impl.UserProfileService; // 确保 service 路径正确
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // 【优化】直接使用 @AuthenticationPrincipal 获取用户ID，更简洁
    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile(@AuthenticationPrincipal Long currentUserId) {
        UserProfile profile = userProfileService.getMyProfile(currentUserId);
        return ResponseEntity.ok(profile);
    }

    // 【优化】直接使用 @AuthenticationPrincipal 获取用户ID
    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateMyProfile(@AuthenticationPrincipal Long currentUserId, @Valid @RequestBody UserProfileDTO userProfileDTO) {
        UserProfile updatedProfile = userProfileService.updateMyProfile(currentUserId, userProfileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    // 【优化】原有的 private getCurrentUserId() 方法已被删除，不再需要

    // 处理头像上传的API接口 (保持不变)
    @PostMapping("/me/avatar")
    public ResponseEntity<?> uploadAvatar(@AuthenticationPrincipal Long currentUserId,
                                          @RequestParam("avatarFile") MultipartFile avatarFile) {
        String newAvatarUrl = userProfileService.updateAvatar(currentUserId, avatarFile);
        return ResponseEntity.ok(Map.of("avatarUrl", newAvatarUrl));
    }

    /**
     * 【新增API端点】
     * 检查当前用户的个人资料是否完整
     * @param currentUserId Spring Security 自动注入的当前用户ID
     * @return 返回一个包含布尔值的JSON，例如 {"isComplete": true}
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getProfileStatus(@AuthenticationPrincipal Long currentUserId) {
        boolean isComplete = userProfileService.isProfileComplete(currentUserId);
        return ResponseEntity.ok(Map.of("isComplete", isComplete));
    }
}