<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>欢迎登录 - 相亲交友</span>
        </div>
      </template>

      <el-form :model="loginForm" ref="loginFormRef" label-width="80px">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="loginForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>

    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建一个响应式对象来存储表单数据
const loginForm = reactive({
  phone: '',
  password: '',
});

// 获取路由实例，用于页面跳转
const router = useRouter();

// 处理登录按钮点击事件
const handleLogin = async () => {
  try {
    // 使用 axios 发送 POST 请求到我们的后端登录接口
    const response = await axios.post('http://localhost:8080/api/auth/login', {
      phone: loginForm.phone,
      password: loginForm.password,
    });

    // 登录成功
    if (response.status === 200) {
      const token = response.data.token;

      // 【关键一步】将获取到的 Token 存储到浏览器的 localStorage 中
      localStorage.setItem('jwt_token', token);

      ElMessage.success('登录成功！');

      // 登录成功后，跳转到首页
      router.push('/');
    }
  } catch (error) {
    // 登录失败
    console.error('登录失败:', error);
    ElMessage.error(error.response?.data?.message || '登录失败，请检查账号密码');
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.login-card {
  width: 450px;
}
.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}
</style>