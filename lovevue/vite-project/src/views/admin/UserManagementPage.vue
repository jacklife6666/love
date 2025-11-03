<template>
  <div class="user-management-container">
    <h1>用户管理</h1>
    <el-table :data="userList" stripe style="width: 100%">
      <el-table-column prop="id" label="用户ID" width="100"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="180"></el-table-column>
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'warning' : 'info'">{{ row.role }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="密码 (加密后)">
        <template #default="{ row }">
          <span class="password-span">{{ visiblePasswords[row.id] || '******************' }}</span>
        </template>
      </el-table-column>

      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="220"></el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button type="primary" link @click="showPassword(row)">查看密码</el-button>
          <el-button type="danger" link>禁用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="管理员身份验证" width="400px">
      <p>为了安全，请输入您的登录密码以查看用户密码：</p>
      <el-input
          v-model="adminPassword"
          type="password"
          show-password
          placeholder="请输入您的登录密码"
          @keyup.enter="handlePasswordVerification"
      ></el-input>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handlePasswordVerification">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

// --- 响应式数据 ---
const userList = ref([]);
const dialogVisible = ref(false);
const adminPassword = ref('');
const currentUserToView = ref(null);
const visiblePasswords = reactive({}); // 使用 reactive 对象来存储已解锁的密码

// 获取所有用户的数据
const fetchUsers = async () => {
  try {
    const token = localStorage.getItem('jwt_token');
    const response = await axios.get('http://localhost:8080/api/admin/users', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    userList.value = response.data;
  } catch (error) {
    console.error("获取用户列表失败:", error);
    ElMessage.error("获取用户列表失败");
  }
};

onMounted(fetchUsers);

// --- 方法 ---
// 1. 点击“查看密码”按钮，弹出对话框
const showPassword = (user) => {
  // 如果密码已经显示，则不再重复验证
  if (visiblePasswords[user.id]) {
    ElMessage.info('密码已显示');
    return;
  }
  currentUserToView.value = user;
  adminPassword.value = ''; // 清空上次输入的密码
  dialogVisible.value = true;
};

// 2. 在对话框中点击“确认”按钮
const handlePasswordVerification = async () => {
  if (!adminPassword.value.trim()) {
    ElMessage.warning('请输入您的密码');
    return;
  }
  try {
    const token = localStorage.getItem('jwt_token');
    // 3. 调用后端接口，验证管理员自己的密码
    await axios.post('http://localhost:8080/api/admin/verify-password',
        { password: adminPassword.value },
        { headers: { 'Authorization': `Bearer ${token}` } }
    );

    // 4. 验证成功，获取目标用户的密码
    const response = await axios.get(`http://localhost:8080/api/admin/user-password/${currentUserToView.value.id}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });

    // 5. 将获取到的密码存起来，Vue会自动更新页面显示
    visiblePasswords[currentUserToView.value.id] = response.data.password;
    dialogVisible.value = false;
    ElMessage.success('验证成功！已显示该用户的加密密码');

  } catch (error) {
    console.error("密码验证失败:", error);
    if (error.response && error.response.status === 401) {
      ElMessage.error('您的密码不正确！');
    } else {
      ElMessage.error('操作失败，请稍后重试');
    }
  }
};
</script>

<style scoped>
.user-management-container {
  padding: 20px;
}
.password-span {
  font-family: 'Courier New', Courier, monospace;
  font-size: 12px;
  color: #606266;
  word-break: break-all;
}
</style>