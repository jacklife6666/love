import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router' // 1. 确保正确导入路由

const app = createApp(App)

app.use(router) // 2. 【核心】确保这行代码存在，它就是“安装”路由的关键
app.use(ElementPlus)

app.mount('#app')