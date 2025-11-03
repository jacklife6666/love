package com.love.datingapp.mapper;
import com.love.datingapp.entity.Swipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SwipeMapper {
    void save(Swipe swipe);
    Swipe findByUserIdAndTargetUserId(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);
}