package com.love.datingapp.mapper;
import com.love.datingapp.common.dto.MatchVO;
import com.love.datingapp.entity.Match;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchMapper {
    void save(Match match);
    // 【新增方法】根据用户ID查询其所有匹配列表
    List<MatchVO> findMatchesByUserId(Long userId);
}