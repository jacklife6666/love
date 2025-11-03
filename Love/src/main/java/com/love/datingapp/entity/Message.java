package com.love.datingapp.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 聊天消息实体类, 对应数据库的 t_messages 表
 */
@Data
public class Message {
    /**
     * 消息ID, 主键
     */
    private Long id;

    /**
     * 发送者用户ID
     */
    private Long senderId;

    /**
     * 接收者用户ID
     */
    private Long receiverId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息发送时间
     */
    private LocalDateTime createTime;

    /**
     * 消息状态: 0-未读, 1-已读
     */
    private Integer status;
}