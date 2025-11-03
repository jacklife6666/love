import { createRouter, createWebHistory } from 'vue-router';
import { jwtDecode } from 'jwt-decode';
import api from '../api'; // 【新增】导入api服务
import { ElMessage } from 'element-plus'; // 【新增】导入ElMessage用于提示

// ... 导入页面组件 ...
import HomePage from '../views/HomePage.vue';
import LoginPage from '../views/LoginPage.vue';
import MatchesPage from '../views/MatchesPage.vue';
import ProfilePage from '../views/ProfilePage.vue';
import ChatPage from '../views/ChatPage.vue';
import AdminDashboardPage from '../views/admin/DashboardPage.vue';
import AdminUserManagementPage from '../views/admin/UserManagementPage.vue';

const routes = [
    // 【修改】为需要检查资料的路由添加一个新的 meta 标志
    { path: '/', name: 'Home', component: HomePage, meta: { requiresAuth: true, requiresProfile: true } },
    { path: '/matches', name: 'Matches', component: MatchesPage, meta: { requiresAuth: true, requiresProfile: true } },
    { path: '/chat/:userId?', name: 'Chat', component: ChatPage, meta: { requiresAuth: true, requiresProfile: true } },

    // 个人中心和登录页不需要检查资料完整性
    { path: '/profile', name: 'Profile', component: ProfilePage, meta: { requiresAuth: true } },
    { path: '/login', name: 'Login', component: LoginPage },

    // ... 管理员路由 ...
    {
        path: '/admin',
        children: [
            { path: 'dashboard', name: 'AdminDashboard', component: AdminDashboardPage, meta: { requiresAuth: true, role: 'ADMIN' } },
            { path: 'users', name: 'AdminUserManagement', component: AdminUserManagementPage, meta: { requiresAuth: true, role: 'ADMIN' } }
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 【核心修改】重写全局路由守卫
router.beforeEach(async (to, from, next) => {
    const token = localStorage.getItem('jwt_token');

    // 1. 判断路由是否需要认证
    if (to.meta.requiresAuth) {
        // 1a. 如果需要认证但没有token，跳转到登录页
        if (!token) {
            return next({ name: 'Login' });
        }

        // 1b. 有token，检查角色权限 (管理员)
        const userRole = jwtDecode(token).role;
        if (to.meta.role && to.meta.role !== userRole) {
            ElMessage.error('您没有权限访问此页面！');
            return next({ name: 'Home' });
        }

        // 2. 【新增】判断路由是否需要完整的个人资料
        if (to.meta.requiresProfile) {
            try {
                // 调用后端接口检查资料状态
                const response = await api.getProfileStatus();
                if (!response.data.isComplete) {
                    // 如果资料不完整，提示并强制跳转到个人资料页
                    ElMessage.warning('为了获得更好的体验，请先完善您的个人资料！');
                    return next({ name: 'Profile' });
                }
            } catch (error) {
                // 如果接口调用失败，跳转到登录页重新认证
                console.error("检查资料状态失败:", error);
                return next({ name: 'Login' });
            }
        }

        // 3. 所有检查通过，允许访问
        return next();

    } else {
        // 路由不需要认证，直接放行
        return next();
    }
});

export default router;