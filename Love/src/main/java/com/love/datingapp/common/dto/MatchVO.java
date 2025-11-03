package com.love.datingapp.common.dto;

import lombok.Data;

@Data
public class MatchVO {
    // 这个是匹配记录本身的ID
    private Long matchId;
    // 这是与你匹配的那个人的用户ID
    private Long matchedUserId;
    // 对方的昵称
    private String nickname;
    // 对方的头像
    private String avatar;
}