package com.love.datingapp.service.impl;


import com.love.datingapp.common.dto.MatchVO;
import java.util.List;

public interface MatchService {
    List<MatchVO> getMyMatches(Long currentUserId);
}