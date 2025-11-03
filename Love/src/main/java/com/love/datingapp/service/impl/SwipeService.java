package com.love.datingapp.service.impl;
import com.love.datingapp.common.dto.SwipeDTO;

public interface SwipeService {
    boolean swipe(Long currentUserId, SwipeDTO swipeDTO);
}