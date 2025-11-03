import { Client } from '@stomp/stompjs';
// 【修正】使用标准的模块导入方式，不再指定具体文件路径
import SockJS from 'sockjs-client';

let stompClient = null;

const webSocketService = {
    connect(callbacks = {}) {
        if (stompClient && stompClient.active) {
            console.log('WebSocket is already connected.');
            return;
        }

        const token = localStorage.getItem('jwt_token');
        if (!token) {
            console.error('No JWT token found for WebSocket connection.');
            return;
        }

        stompClient = new Client({
            // 这里 webSocketFactory 的拼写和用法是正确的
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            connectHeaders: {
                Authorization: `Bearer ${token}`,
            },
            // 【修正】移除了未使用的 str 参数
            debug: (msg) => { /* console.log(new Date(), msg); */ },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);

            if (callbacks.onChatMessage) {
                stompClient.subscribe('/user/queue/messages', (message) => {
                    callbacks.onChatMessage(JSON.parse(message.body));
                });
            }

            if (callbacks.onNotification) {
                stompClient.subscribe('/user/queue/notifications', (notification) => {
                    callbacks.onNotification(JSON.parse(notification.body));
                });
            }
        };

        stompClient.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

        stompClient.activate();
    },

    // publish 方法是 Client 类的标准方法
    sendMessage({ receiverId, content }) {
        if (stompClient && stompClient.active) {
            stompClient.publish({
                destination: '/app/chat.sendMessage',
                body: JSON.stringify({ receiverId, content }),
            });
        } else {
            console.error('STOMP client is not connected.');
        }
    },

    // deactivate 方法是 Client 类的标准方法
    disconnect() {
        if (stompClient && stompClient.active) {
            stompClient.deactivate();
        }
        stompClient = null;
        console.log('WebSocket disconnected.');
    },
};

export default webSocketService;