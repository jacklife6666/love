package com.love.datingapp.mapper;

import com.love.datingapp.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    /**
     * 保存一条聊天消息
     * @param message 消息实体
     * @return 影响的行数
     */
    int save(Message message);
}