package com.love.datingapp.common.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long receiverId; // 消息接收者的用户ID
    private String content;  // 消息内容
}