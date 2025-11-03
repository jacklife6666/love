package com.love.datingapp.service.impl;

import com.love.datingapp.common.dto.SwipeDTO;
import com.love.datingapp.entity.Match;
import com.love.datingapp.entity.Swipe;
import com.love.datingapp.entity.UserProfile;
import com.love.datingapp.mapper.MatchMapper;
import com.love.datingapp.mapper.SwipeMapper;
import com.love.datingapp.mapper.UserProfileMapper;
import com.love.datingapp.service.impl.SwipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate; // 【新增】导入
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit; // 【新增】导入

@Service
public class SwipeServiceImpl implements SwipeService {

    // 【新增】定义 Redis Key 的前缀
    public static final String KEY_PREFIX_SWIPED = "swiped:user:";

    @Autowired
    private SwipeMapper swipeMapper;

    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserProfileMapper userProfileMapper;

    // 【新增】注入 RedisTemplate
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public boolean swipe(Long currentUserId, SwipeDTO swipeDTO) {
        // 1. 保存当前的滑动记录 (数据库)
        Swipe swipe = new Swipe();
        swipe.setUserId(currentUserId);
        swipe.setTargetUserId(swipeDTO.getTargetUserId());
        swipe.setSwipeType(swipeDTO.getSwipeType());
        swipeMapper.save(swipe);

        // 2. 【核心新增】将这次滑动记录也存入 Redis
        String key = KEY_PREFIX_SWIPED + currentUserId;
        // 我们把被滑动的用户ID，添加到 "swiped:user:{id}" 这个 Set 集合中
        redisTemplate.opsForSet().add(key, swipeDTO.getTargetUserId());
        // 【可选优化】给这个集合设置一个过期时间，比如30天，防止它无限增大
        redisTemplate.expire(key, 30, TimeUnit.DAYS);

        // 3. 如果是“不喜欢”，直接返回
        if ("pass".equals(swipeDTO.getSwipeType())) {
            return false;
        }

        // 4. 如果是“喜欢”，则检查对方是否也“喜欢”了自己
        Swipe reverseSwipe = swipeMapper.findByUserIdAndTargetUserId(swipeDTO.getTargetUserId(), currentUserId);

        if (reverseSwipe != null && "like".equals(reverseSwipe.getSwipeType())) {
            // 5. 互相喜欢，匹配成功！
            Match match = new Match();
            if (currentUserId < swipeDTO.getTargetUserId()) {
                match.setUser1Id(currentUserId);
                match.setUser2Id(swipeDTO.getTargetUserId());
            } else {
                match.setUser1Id(swipeDTO.getTargetUserId());
                match.setUser2Id(currentUserId);
            }
            matchMapper.save(match);

            // 6. 发送实时匹配通知
            sendMatchNotification(currentUserId, swipeDTO.getTargetUserId());
            return true;
        }

        return false;
    }

    // sendMatchNotification 方法 (保持不变)
    private void sendMatchNotification(Long userId1, Long userId2) {
        // ... (您已有的通知逻辑) ...
    }
}