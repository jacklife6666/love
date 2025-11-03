<template>
  <div class="matches-container">
    <h1 class="title">我的匹配</h1>
    <div v-if="matches.length > 0" class="matches-list">
      <el-card v-for="match in matches" :key="match.matchId" class="match-card" shadow="hover">
        <div class="match-content">
          <el-avatar :size="60" :src="match.avatar ? `http://localhost:8080${match.avatar}` : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" @error="() => true">
            <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="默认头像"/>
          </el-avatar>
          <div class="match-info">
            <span class="nickname">{{ match.nickname }}</span>
          </div>
          <el-button type="primary" plain @click="startChat(match)">开始聊天</el-button>
        </div>
      </el-card>
    </div>
    <div v-else class="no-matches">
      <p>你还没有任何匹配，快去首页右滑喜欢吧！</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const matches = ref([]);
const router = useRouter();

// 【修正】将 onMounted 变为异步函数，以便在内部使用 await
onMounted(async () => {
  // 【修正】调用异步函数时，推荐使用 await
  await fetchMyMatches();
});

const fetchMyMatches = async () => {
  try {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      await router.push('/login');
      return;
    }

    const response = await axios.get('http://localhost:8080/api/matches', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    matches.value = response.data;

  } catch (error) {
    console.error('获取匹配列表失败:', error);
    ElMessage.error('获取匹配列表失败，请稍后重试');
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      await router.push('/login');
    }
  }
};

// 【修正】将 startChat 变为异步函数，以便在内部使用 await
const startChat = async (matchUser) => {
  // 【修正】调用 router.push 时，推荐使用 await
  await router.push({
    name: 'Chat',
    params: {
      userId: matchUser.matchedUserId,
    }
  });
}
</script>

<style scoped>
/* 样式部分无需改动 */
.matches-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}
.title {
  text-align: center;
  color: #303133;
  margin-bottom: 30px;
}
.matches-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.match-card {
  border-radius: 10px;
}
.match-content {
  display: flex;
  align-items: center;
  gap: 15px;
}
.match-info {
  flex-grow: 1;
}
.nickname {
  font-size: 18px;
  font-weight: bold;
}
.no-matches {
  text-align: center;
  margin-top: 50px;
  color: #909399;
}
</style>