package com.love.datingapp.controller;

import com.love.datingapp.common.dto.ChatMessageDTO;
import com.love.datingapp.entity.Message; // 【新增】导入 Message 实体类
import com.love.datingapp.service.impl.ChatService; // 【新增】导入我们之前创建的 ChatService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller // 注意，这里是 @Controller，不是 @RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Spring提供的用于发送消息的模板

    // 【新增】注入用于处理消息存储的Service
    @Autowired
    private ChatService chatService;

    /**
     * 处理从客户端发送来的聊天消息
     * @MessageMapping("/chat.sendMessage") 定义了消息的目标地址。
     * 前端发送消息到 "/app/chat.sendMessage" 时，这个方法就会被调用。
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage, Principal principal) {
        // principal 对象包含了当前 WebSocket 连接的用户信息，它的 name 就是我们在JWT过滤器里设置的用户ID
        Long senderId = Long.valueOf(principal.getName());

        System.out.println("收到来自用户 " + senderId + " 发往用户 " + chatMessage.getReceiverId() + " 的消息: " + chatMessage.getContent());

        // 【修改部分开始】
        // 第一步：将消息保存到数据库
        Message messageToSave = new Message();
        messageToSave.setSenderId(senderId);
        messageToSave.setReceiverId(chatMessage.getReceiverId());
        messageToSave.setContent(chatMessage.getContent());
        chatService.saveMessage(messageToSave);
        // 【修改部分结束】


        // 原有逻辑保持不变：将消息实时发送到特定用户的 "队列" 中
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getReceiverId()), // 目标用户的ID
                "/queue/messages", // 目标队列
                // 实际发送的消息内容，可以是一个更复杂的对象
                Map.of(
                        "senderId", senderId,
                        "content", chatMessage.getContent()
                )
        );

        // 原来的 TODO 任务已经完成
    }
}