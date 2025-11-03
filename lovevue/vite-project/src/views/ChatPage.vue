<template>
  <div class="chat-container">
    <div class="user-list">
      <div class="list-header">我的匹配</div>
      <div v-if="matches.length === 0" class="no-matches">暂无匹配</div>
      <div
          v-for="user in matches"
          :key="user.matchedUserId"
          class="user-item"
          :class="{ active: selectedUser && selectedUser.matchedUserId === user.matchedUserId }"
          @click="selectUser(user)"
      >
        <img :src="user.avatar || defaultAvatar" alt="avatar" class="avatar" />
        <span class="nickname">{{ user.nickname }}</span>
      </div>
    </div>
    <div class="chat-window">
      <template v-if="selectedUser">
        <div class="chat-header">{{ selectedUser.nickname }}</div>
        <div class="message-area" ref="messageAreaRef">
          <div
              v-for="(msg, index) in messages"
              :key="index"
              class="message-bubble"
              :class="msg.senderId === currentUserId ? 'sent' : 'received'"
          >
            <p class="message-content">{{ msg.content }}</p>
          </div>
        </div>
        <div class="message-input">
          <textarea v-model="newMessage" @keyup.enter.prevent="sendMessage" placeholder="输入消息..."></textarea>
          <button @click="sendMessage">发送</button>
        </div>
      </template>
      <div v-else class="no-chat-selected">
        <p>欢迎来到聊天室</p>
        <p>请从左侧选择一位好友开始聊天吧！</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue';
import { useRoute } from 'vue-router';
import { jwtDecode } from 'jwt-decode';
import api from '../api';
import webSocketService from '../utils/websocket';
import defaultAvatar from '../assets/default-avatar.png';

const route = useRoute();
const matches = ref([]);
const selectedUser = ref(null);
const messages = ref([]);
const newMessage = ref('');
const messageAreaRef = ref(null);

const currentUserId = computed(() => {
  const token = localStorage.getItem('jwt_token');
  return token ? jwtDecode(token).userId : null;
});

const scrollToBottom = () => {
  nextTick(() => {
    const area = messageAreaRef.value;
    if (area) {
      area.scrollTop = area.scrollHeight;
    }
  });
};

const fetchMatches = async () => {
  try {
    const response = await api.getMyMatches();
    matches.value = response.data;
    const targetUserId = route.params.userId;
    if (targetUserId) {
      const userToSelect = matches.value.find(
          (user) => user.matchedUserId == targetUserId
      );
      if (userToSelect) {
        selectUser(userToSelect);
      }
    }
  } catch (error) {
    console.error('获取匹配列表失败:', error);
  }
};

const selectUser = async (user) => {
  selectedUser.value = user;
  messages.value = [];
  try {
    const response = await api.getChatHistory(user.matchedUserId);
    messages.value = response.data;
    scrollToBottom();
  } catch (error) {
    console.error('获取历史消息失败:', error);
  }
};

const sendMessage = () => {
  if (!newMessage.value.trim() || !selectedUser.value) return;
  const messagePayload = {
    receiverId: selectedUser.value.matchedUserId,
    content: newMessage.value,
  };
  webSocketService.sendMessage(messagePayload);
  messages.value.push({
    senderId: currentUserId.value,
    content: newMessage.value,
  });
  newMessage.value = '';
  scrollToBottom();
};

const handleIncomingMessage = (message) => {
  if (selectedUser.value && message.senderId === selectedUser.value.matchedUserId) {
    messages.value.push(message);
    scrollToBottom();
  }
};

onMounted(() => {
  fetchMatches();
  webSocketService.connect({
    onChatMessage: handleIncomingMessage
  });
});

onUnmounted(() => {
  webSocketService.disconnect();
});
</script>

<style scoped>
.chat-container { display: flex; height: calc(100vh - 100px); border: 1px solid #dcdfe6; border-radius: 4px; }
.user-list { width: 250px; border-right: 1px solid #dcdfe6; display: flex; flex-direction: column; overflow-y: auto; }
.list-header { padding: 15px; font-weight: bold; border-bottom: 1px solid #dcdfe6; text-align: center; position: sticky; top: 0; background: white; }
.no-matches { padding: 20px; text-align: center; color: #909399; }
.user-item { display: flex; align-items: center; padding: 10px 15px; cursor: pointer; transition: background-color 0.2s; }
.user-item:hover { background-color: #f5f7fa; }
.user-item.active { background-color: #ecf5ff; }
.avatar { width: 40px; height: 40px; border-radius: 50%; margin-right: 10px; object-fit: cover; }
.nickname { font-size: 14px; }
.chat-window { flex: 1; display: flex; flex-direction: column; }
.chat-header { padding: 15px; border-bottom: 1px solid #dcdfe6; font-weight: bold; }
.message-area { flex: 1; padding: 20px; overflow-y: auto; background-color: #f5f7fa; }
.message-bubble { margin-bottom: 15px; max-width: 70%; display: flex; }
.message-content { padding: 10px 15px; border-radius: 18px; display: inline-block; word-wrap: break-word; margin: 0; }
.received { justify-content: flex-start; }
.received .message-content { background-color: #ffffff; border: 1px solid #e4e7ed; }
.sent { justify-content: flex-end; }
.sent .message-content { background-color: #409eff; color: white; }
.message-input { display: flex; padding: 10px; border-top: 1px solid #dcdfe6; }
.message-input textarea { flex: 1; resize: none; border-radius: 4px; border: 1px solid #dcdfe6; padding: 8px; font-family: inherit; font-size: 14px; height: 60px; }
.message-input button { margin-left: 10px; padding: 0 20px; border: none; background-color: #409eff; color: white; border-radius: 4px; cursor: pointer; }
.no-chat-selected { flex: 1; display: flex; flex-direction: column; justify-content: center; align-items: center; color: #909399; background-color: #f5f7fa; }
.no-chat-selected p { margin: 5px 0; }
</style>