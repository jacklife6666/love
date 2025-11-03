package com.love.datingapp.mapper;

import com.love.datingapp.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileMapper {
    UserProfile findByUserId(Long userId);
    int save(UserProfile userProfile);
    int update(UserProfile userProfile);
}