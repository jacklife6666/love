package com.love.datingapp.security.websocket;

import com.love.datingapp.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // 1. 判断是否是首次连接请求
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 2. 从连接头中获取我们前端设置的 Token
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                // 3. 校验 Token 并获取用户ID和角色
                Long userId = jwtUtils.validateTokenAndGetUserId(token);
                String role = jwtUtils.validateTokenAndGetRole(token);

                if (userId != null && role != null) {
                    // 4. 如果用户有效，就创建一个认证对象
                    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + role);
                    // 【关键】这里的 Principal 的 name 会被设置为 userId，
                    // 这样在 @MessageMapping 方法中就能通过 Principal.getName() 获取到
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            String.valueOf(userId), null, authorities
                    );
                    // 5. 将认证信息存入 accessor，关联到当前的 WebSocket Session
                    accessor.setUser(authentication);
                }
            }
        }
        return message;
    }
}