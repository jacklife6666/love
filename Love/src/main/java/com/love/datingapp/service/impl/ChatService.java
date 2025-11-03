package com.love.datingapp.service.impl;

import com.love.datingapp.entity.Message;
import com.love.datingapp.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 保存聊天消息到数据库
     * @param message 消息实体
     */
    @Transactional
    public void saveMessage(Message message) {
        messageMapper.save(message);
    }
}