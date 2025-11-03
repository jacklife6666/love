package com.love.datingapp.service.impl;

import com.love.datingapp.common.dto.UserProfileDTO;
import com.love.datingapp.entity.UserProfile;
import com.love.datingapp.mapper.UserProfileMapper;
import com.love.datingapp.service.impl.UserProfileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate; // 【新增】导入
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit; // 【新增】导入

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileMapper userProfileMapper;

    // 【新增】注入我们刚刚配置好的 RedisTemplate
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 【新增】定义缓存 Key 的前缀
    public static final String KEY_PREFIX = "user:profile:";

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    /**
     * 【核心修改】
     * 1. 增加缓存读取逻辑
     * 2. 增加缓存写入逻辑
     */
    @Override
    public UserProfile getMyProfile(Long userId) {
        String key = KEY_PREFIX + userId;

        // 1. 优先从 Redis 缓存中读取
        UserProfile profile = (UserProfile) redisTemplate.opsForValue().get(key);

        if (profile != null) {
            // 缓存命中，直接返回
            System.out.println("缓存命中，从 Redis 读取: " + key);
            return profile;
        }

        // 2. 缓存未命中，从 MySQL 数据库中读取
        System.out.println("缓存未命中，从 MySQL 读取: " + key);
        profile = userProfileMapper.findByUserId(userId);

        if (profile == null) {
            // 数据库中也不存在，返回一个空对象（防止缓存穿透）
            profile = new UserProfile();
        }

        // 3. 将从数据库中读到的数据，写入 Redis 缓存，并设置1小时过期
        redisTemplate.opsForValue().set(key, profile, 1, TimeUnit.HOURS);

        return profile;
    }

    /**
     * 【核心修改】
     * 增加“删除缓存”逻辑，保证数据一致性
     */
    @Override
    public UserProfile updateMyProfile(Long userId, UserProfileDTO userProfileDTO) {
        UserProfile existingProfile = userProfileMapper.findByUserId(userId);
        if (existingProfile == null) {
            // ... (原有的创建逻辑)
            existingProfile = new UserProfile();
            BeanUtils.copyProperties(userProfileDTO, existingProfile);
            existingProfile.setUserId(userId);
            userProfileMapper.save(existingProfile);
        } else {
            // 1. 先更新数据库
            BeanUtils.copyProperties(userProfileDTO, existingProfile);
            existingProfile.setUserId(userId);
            userProfileMapper.update(existingProfile);
        }

        // 2. 【新增】从 Redis 中删除缓存
        redisTemplate.delete(KEY_PREFIX + userId);
        System.out.println("更新资料，删除缓存: " + KEY_PREFIX + userId);

        return existingProfile;
    }

    /**
     * 【核心修改】
     * 增加“删除缓存”逻辑
     */
    @Override
    public String updateAvatar(Long userId, MultipartFile avatarFile) {
        // ... (原有的文件校验和保存逻辑) ...
        if (avatarFile == null || avatarFile.isEmpty()) { /* ... */ }
        if (avatarFile.getSize() > MAX_FILE_SIZE) { /* ... */ }
        String contentType = avatarFile.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) { /* ... */ }
        String originalFilename = avatarFile.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        File dest = new File(UPLOAD_DIR + newFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            avatarFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败", e);
        }

        String avatarUrl = "/uploads/" + newFileName;

        // 1. 先更新数据库
        UserProfile userProfile = userProfileMapper.findByUserId(userId);
        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUserId(userId);
            userProfile.setAvatar(avatarUrl);
            userProfileMapper.save(userProfile);
        } else {
            userProfile.setAvatar(avatarUrl);
            userProfileMapper.update(userProfile);
        }

        // 2. 【新增】从 Redis 中删除缓存
        redisTemplate.delete(KEY_PREFIX + userId);
        System.out.println("更新头像，删除缓存: " + KEY_PREFIX + userId);

        return avatarUrl;
    }

    @Override
    public boolean isProfileComplete(Long userId) {
        // 这个方法我们也可以从缓存中读取，优化效率
        UserProfile userProfile = this.getMyProfile(userId); // 复用 getMyProfile 的缓存逻辑

        if (userProfile == null || userProfile.getUserId() == null) {
            return false;
        }
        return StringUtils.hasText(userProfile.getNickname()) &&
                userProfile.getGender() != null &&
                userProfile.getBirthdate() != null;
    }
}