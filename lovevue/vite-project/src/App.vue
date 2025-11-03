<template>
  <div id="app-layout">
    <header class="main-header" v-if="userRole">
      <div class="logo">相亲交友</div>
      <nav class="main-nav">
        <router-link to="/">首页</router-link>
        <router-link to="/matches">我的匹配</router-link>
        <router-link to="/chat">在线聊天</router-link>
        <router-link to="/profile">个人中心</router-link>

        <template v-if="userRole === 'ADMIN'">
          <router-link to="/admin/dashboard">后台首页</router-link>
          <router-link to="/admin/users">用户管理</router-link>
        </template>
        <a href="#" @click.prevent="logout">退出登录</a>
      </nav>
    </header>
    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
// 【修改】从 vue 导入 onMounted
import { ref, watch, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { jwtDecode } from 'jwt-decode';
// 【新增】导入 Element Plus 的通知组件和我们的 WebSocket 服务
import { ElNotification } from 'element-plus';
import webSocketService from './utils/websocket';

const router = useRouter();
const route = useRoute();
const userRole = ref(null);

// 【新增】处理新匹配通知的函数
const handleNewMatchNotification = (notification) => {
  // 确保收到的消息类型是新匹配
  if (notification.type === 'NEW_MATCH') {
    ElNotification({
      title: '匹配成功！',
      message: notification.message,
      type: 'success',
      duration: 5000, // 通知显示5秒
      // 【新增】点击通知，可以直接跳转到聊天页面
      onClick: () => {
        router.push(`/chat/${notification.matchedUser.userId}`);
      },
    });
  }
};

// 您原有的 checkUserRole 函数 (保持不变)
const checkUserRole = () => {
  const token = localStorage.getItem('jwt_token');
  if (token) {
    try {
      const decodedToken = jwtDecode(token);
      userRole.value = decodedToken.role;
    } catch (e) {
      console.error("Token 解码失败:", e);
      logout();
    }
  } else {
    userRole.value = null;
  }
};

// 【修改】您原有的 logout 函数，增加了断开 WebSocket 的逻辑
const logout = () => {
  localStorage.removeItem('jwt_token');
  userRole.value = null;
  webSocketService.disconnect(); // 退出登录时断开连接
  router.push('/login');
};

// 您原有的 watch 逻辑 (保持不变)
watch(route, () => {
  checkUserRole();
}, { immediate: true });

// 【新增】当组件挂载时，如果用户已登录，则连接 WebSocket
onMounted(() => {
  const token = localStorage.getItem('jwt_token');
  if (token) {
    // 连接 WebSocket，并传入处理通知的回调函数
    webSocketService.connect({
      onNotification: handleNewMatchNotification
    });
  }
});
</script>

<style>
/* 您的样式部分完全不需要改动 */
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}
.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
}
.logo {
  font-size: 20px;
  font-weight: bold;
}
.main-nav a {
  text-decoration: none;
  color: #606266;
  margin-left: 20px;
  font-weight: 500;
}
.main-nav a.router-link-exact-active {
  color: #409eff;
}
.main-content {
  padding: 20px;
}
</style>