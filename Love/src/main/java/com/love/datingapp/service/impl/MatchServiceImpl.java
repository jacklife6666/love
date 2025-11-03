package com.love.datingapp.service.impl;



import com.love.datingapp.common.dto.MatchVO;
import com.love.datingapp.mapper.MatchMapper;
import com.love.datingapp.service.impl.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchMapper matchMapper;

    @Override
    public List<MatchVO> getMyMatches(Long currentUserId) {
        return matchMapper.findMatchesByUserId(currentUserId);
    }
}
