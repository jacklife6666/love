<template>
  <div class="profile-container">
    <el-alert
        v-if="!isProfileComplete"
        title="请完善您的个人资料"
        type="warning"
        description="为了让其他用户更好地了解您，请填写所有带星号(*)的必填项。"
        show-icon
        :closable="false"
        class="profile-alert"
    />

    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人中心</span>
          <el-button v-if="!isEditing" type="primary" link @click="enterEditMode">编辑资料</el-button>
        </div>
      </template>

      <div class="avatar-section">
        <el-avatar :size="100" :src="profile.fullAvatarUrl" />
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/api/profiles/me/avatar"
            :headers="uploadHeaders"
            name="avatarFile"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
        >
          <el-button type="primary">点击上传头像</el-button>
        </el-upload>
      </div>

      <div v-if="!isEditing">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="昵称">{{ profile.nickname }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ profile.gender === 1 ? '男' : profile.gender === 0 ? '女' : '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="生日">{{ profile.birthdate }}</el-descriptions-item>
          <el-descriptions-item label="城市">{{ profile.city }}</el-descriptions-item>
          <el-descriptions-item label="职业">{{ profile.occupation }}</el-descriptions-item>
          <el-descriptions-item label="个人简介">{{ profile.introduction }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div v-else>
        <el-form :model="editForm" label-width="80px">
          <el-form-item label="* 昵称">
            <el-input v-model="editForm.nickname"></el-input>
          </el-form-item>
          <el-form-item label="* 性别">
            <el-radio-group v-model="editForm.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="* 生日">
            <el-date-picker v-model="editForm.birthdate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD"></el-date-picker>
          </el-form-item>
          <el-form-item label="城市">
            <el-input v-model="editForm.city"></el-input>
          </el-form-item>
          <el-form-item label="职业">
            <el-input v-model="editForm.occupation"></el-input>
          </el-form-item>
          <el-form-item label="个人简介">
            <el-input v-model="editForm.introduction" type="textarea"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveProfile">保存</el-button>
            <el-button @click="cancelEdit">取消</el-button>
          </el-form-item>
        </el-form>
      </div>

    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import api from '../api';

const profile = ref({ fullAvatarUrl: '' });
const router = useRouter();
const uploadHeaders = reactive({ Authorization: '' });
const isEditing = ref(false);
const editForm = reactive({});
const isProfileComplete = ref(true);

const API_BASE_URL = 'http://localhost:8080';
const DEFAULT_AVATAR_URL = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

onMounted(() => {
  const token = localStorage.getItem('jwt_token');
  if (!token) {
    router.push('/login');
    return;
  }
  uploadHeaders.Authorization = `Bearer ${token}`;
  fetchProfileData();
  checkProfileStatus();
});

const checkProfileStatus = async () => {
  try {
    const response = await api.getProfileStatus();
    isProfileComplete.value = response.data.isComplete;
  } catch (error) {
    console.error("检查资料状态失败", error);
  }
};

const processProfileData = (data) => {
  return {
    ...data,
    fullAvatarUrl: data.avatar ? `${API_BASE_URL}${data.avatar}` : DEFAULT_AVATAR_URL
  };
};

const fetchProfileData = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/profiles/me`, {
      headers: { 'Authorization': uploadHeaders.Authorization }
    });
    profile.value = processProfileData(response.data);
  } catch (error) {
    ElMessage.error("获取个人资料失败");
  }
};

const enterEditMode = () => {
  Object.assign(editForm, profile.value);
  isEditing.value = true;
};

const cancelEdit = () => {
  isEditing.value = false;
};

const saveProfile = async () => {
  try {
    const response = await axios.put(`${API_BASE_URL}/api/profiles/me`, editForm, {
      headers: { 'Authorization': uploadHeaders.Authorization }
    });
    profile.value = processProfileData(response.data);
    ElMessage.success('个人资料更新成功!');
    isEditing.value = false;
    await checkProfileStatus();
  } catch (error) {
    ElMessage.error("更新个人资料失败");
  }
};

const handleAvatarSuccess = (response) => {
  ElMessage.success('头像上传成功!');
  // response.avatarUrl 就是后端返回的 /uploads/xxxx.png
  profile.value.avatar = response.avatarUrl;
  // 拼接成完整的URL
  profile.value.fullAvatarUrl = `${API_BASE_URL}${response.avatarUrl}`;
};

const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('头像图片只能是 JPG 或 PNG 格式!');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('头像图片大小不能超过 2MB!');
    return false;
  }
  return true;
};
</script>

<style scoped>
.profile-container { max-width: 600px; margin: 20px auto; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.avatar-section { text-align: center; margin-bottom: 20px; }
.avatar-uploader { margin-top: 15px; }
.profile-alert { margin-bottom: 20px; }
</style>