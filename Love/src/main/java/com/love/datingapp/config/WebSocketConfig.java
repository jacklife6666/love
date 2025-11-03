package com.love.datingapp.config;

import com.love.datingapp.security.websocket.AuthChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 开启 WebSocket 消息代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 【第1步】注入我们自定义的通道拦截器
    @Autowired
    private AuthChannelInterceptor authChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个 STOMP 端点，名为 "/ws"，前端就是通过这个路径来连接 WebSocket 服务器的。
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置消息代理，用于路由消息
        registry.enableSimpleBroker("/topic", "/user");
        // 定义客户端发送消息的目标前缀
        registry.setApplicationDestinationPrefixes("/app");
        // 定义点对点消息的前缀
        registry.setUserDestinationPrefix("/user");
    }

    // 【第2步】注册我们的拦截器，让它在处理消息前生效
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);
    }
}