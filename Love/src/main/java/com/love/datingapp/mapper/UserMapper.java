package com.love.datingapp.mapper;

import com.love.datingapp.common.dto.UserVO;
import com.love.datingapp.entity.User;
import com.love.datingapp.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {

    User findByPhone(String phone);

    int save(User user);

    // 【修改】将 'limit' 改名为 'candidateLimit'，表示这是“候选人”的数量
    List<UserProfile> findRecommendations(@Param("currentUserId") Long currentUserId, @Param("candidateLimit") int candidateLimit);

    List<UserVO> findAllUsers();

    User findById(Long id);
}