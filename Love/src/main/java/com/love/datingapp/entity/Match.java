package com.love.datingapp.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Match {
    private Long id;
    private Long user1Id;
    private Long user2Id;
    private String status;
    private LocalDateTime createTime;
}