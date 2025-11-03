package com.love.datingapp.entity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Swipe {
    private Long id;
    private Long userId;
    private Long targetUserId;
    private String swipeType;
    private LocalDateTime createTime;
}