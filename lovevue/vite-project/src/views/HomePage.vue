<template>
  <div class="home-container">
    <h1 class="title">发现新朋友</h1>

    <div v-if="userList.length > 0" class="card-stack">
      <el-card v-for="user in userList" :key="user.userId" class="user-card" shadow="hover">
        <img :src="user.avatar" class="user-avatar" alt="User Avatar" @error="setDefaultAvatar"/>

        <div class="user-info">
          <h3>{{ user.nickname }}, {{ calculateAge(user.birthdate) }}</h3>
          <p v-if="user.city || user.occupation">{{ user.city }} | {{ user.occupation }}</p>
          <p class="introduction">{{ user.introduction || '这位用户很神秘，什么也没留下...' }}</p>
        </div>

        <div class="actions">
          <el-button type="danger" circle size="large" @click="swipe('pass', user.userId)">❌</el-button>
          <el-button type="success" circle size="large" @click="swipe('like', user.userId)">❤️</el-button>
        </div>
      </el-card>
    </div>

    <div v-else class="no-more-users">
      <p>暂时没有更多推荐了，稍后再来看看吧！</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const userList = ref([]);
const router = useRouter();

onMounted(() => {
  fetchRecommendations();
});

const fetchRecommendations = async () => {
  try {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      router.push('/login');
      return;
    }
    const response = await axios.get('http://localhost:8080/api/users/recommendations', {
      headers: { 'Authorization': `Bearer ${token}` },
      params: { limit: 10 }
    });
    userList.value = response.data;
  } catch (error) {
    console.error('获取推荐用户失败:', error);
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      router.push('/login');
    }
  }
};

const swipe = async (type, targetUserId) => {
  try {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      router.push('/login');
      return;
    }

    const response = await axios.post('http://localhost:8080/api/swipes',
        {
          targetUserId: targetUserId,
          swipeType: type
        },
        {
          headers: { 'Authorization': `Bearer ${token}` }
        }
    );

    userList.value = userList.value.filter(user => user.userId !== targetUserId);

    if (response.data.matched) {
      ElMessage.success('恭喜，匹配成功！可以开始聊天了！');
    }
  } catch (error) {
    console.error('滑动操作失败:', error);
    ElMessage.error('操作失败，请稍后再试');
  }
};

const calculateAge = (birthdate) => {
  if (!birthdate) return '';
  const birthDate = new Date(birthdate);
  const today = new Date();
  let age = today.getFullYear() - birthDate.getFullYear();
  const m = today.getMonth() - birthDate.getMonth();
  if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
    age--;
  }
  return age;
};

const setDefaultAvatar = (event) => {
  event.target.src = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
}
</script>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}
.title {
  color: #303133;
  margin-bottom: 20px;
}
.user-card {
  width: 350px;
  height: 520px;
  border-radius: 15px;
  position: relative;
  margin-bottom: 20px;
}
.user-avatar {
  width: 100%;
  height: 300px;
  object-fit: cover;
}
.user-info {
  padding: 15px;
}
.user-info h3 {
  margin-top: 0;
}
.introduction {
  font-size: 14px;
  color: #606266;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.actions {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 40px;
}
.actions .el-button {
  font-size: 28px;
  width: 70px;
  height: 70px;
}
.no-more-users {
  margin-top: 50px;
  color: #909399;
}
</style>