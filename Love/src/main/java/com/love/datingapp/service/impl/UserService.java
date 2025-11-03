package com.love.datingapp.service.impl;

import com.love.datingapp.entity.UserProfile;
import java.util.List;

public interface UserService {
    /**
     * 获取推荐列表
     * @param currentUserId 当前用户ID
     * @param limit 最终需要返回的数量
     * @return 用户资料列表
     */
    List<UserProfile> getRecommendations(Long currentUserId, int limit);
}