import axios from 'axios';

// 创建一个axios实例，用于API请求
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api', // 您的后端API地址
    headers: {
        'Content-Type': 'application/json',
    },
});

// 添加请求拦截器，在每个请求头发起前，自动附上token
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('jwt_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 导出封装好的API函数
export default {
    // 获取我的匹配列表
    getMyMatches() {
        return apiClient.get('/matches');
    },
    // 获取与指定用户的聊天记录
    getChatHistory(targetUserId) {
        return apiClient.get(`/messages/history/${targetUserId}`);
    },
    // 【新增】获取当前用户的个人资料完整状态
    getProfileStatus() {
        return apiClient.get('/profiles/status');
    },
};