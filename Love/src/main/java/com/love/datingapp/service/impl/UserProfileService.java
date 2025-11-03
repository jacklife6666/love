package com.love.datingapp.service.impl;


import com.love.datingapp.common.dto.UserProfileDTO;
import com.love.datingapp.entity.UserProfile;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {

    /**
     * 获取当前用户的个人资料
     * @param userId 当前登录用户的ID
     * @return 用户资料实体
     */
    UserProfile getMyProfile(Long userId);

    /**
     * 更新当前用户的个人资料
     * @param userId 当前登录用户的ID
     * @param userProfileDTO 前端传来的新资料
     * @return 更新后的用户资料实体
     */
    UserProfile updateMyProfile(Long userId, UserProfileDTO userProfileDTO);

    /**
     * 处理头像上传
     * @param userId 当前登录用户的ID
     * @param avatarFile 头像文件
     * @return 新头像的访问路径
     */
    String updateAvatar(Long userId, MultipartFile avatarFile);

    /**
     * 【新增方法】检查用户的个人资料是否完整
     * @param userId 要检查的用户ID
     * @return 如果资料完整则返回 true，否则返回 false
     */
    boolean isProfileComplete(Long userId);
}