package com.love.datingapp.service.impl;

import com.love.datingapp.entity.UserProfile;
import com.love.datingapp.mapper.UserMapper;
import com.love.datingapp.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 我们从数据库一次性捞 100 个候选人
    private static final int CANDIDATE_LIMIT = 100;

    /**
     * 【核心】获取推荐列表的全新实现
     * @param currentUserId 当前用户ID
     * @param limit 最终需要返回的数量 (例如: 10)
     * @return 用户资料列表
     */
    @Override
    public List<UserProfile> getRecommendations(Long currentUserId, int limit) {

        // 1. 定义该用户 "已滑过" 集合的 Key
        String swipeKey = SwipeServiceImpl.KEY_PREFIX_SWIPED + currentUserId;

        // 2. 从数据库快速“盲抓” 100 个候选人 (SQL 很快)
        List<UserProfile> candidates = userMapper.findRecommendations(currentUserId, CANDIDATE_LIMIT);

        // 3. 准备一个空列表，用来存放最终的推荐结果
        List<UserProfile> recommendations = new ArrayList<>();

        System.out.println("--- [推荐算法] ---");
        System.out.println("从数据库获取到 " + candidates.size() + " 个候选人");

        // 4. 【核心】在 Java 内存中进行过滤
        for (UserProfile candidate : candidates) {
            // 5. 去 Redis 高速检查这个候选人是否在 "已滑过" 集合中
            Boolean hasSwiped = redisTemplate.opsForSet().isMember(swipeKey, candidate.getUserId());

            if (hasSwiped == null || !hasSwiped) {
                // 6. 如果 Redis 说“没滑过”，就把他加入到最终推荐列表
                recommendations.add(candidate);

                // 7. 如果最终列表已经凑够 10 个人了，就没必要再循环了
                if (recommendations.size() >= limit) {
                    break;
                }
            } else {
                // (可选日志) 打印被过滤掉的用户
                System.out.println("过滤掉已滑过的用户: " + candidate.getUserId());
            }
        }

        System.out.println("最终返回 " + recommendations.size() + " 个推荐");
        System.out.println("-----------------");

        return recommendations;
    }
}