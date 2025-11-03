import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  // 【新增】定义全局常量替换
  define: {
    global: 'window', // 将代码中所有的 'global' 关键字替换为 'window'
  },
})