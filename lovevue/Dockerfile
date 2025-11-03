# ---------------------------------------------------
# 阶段 1: 构建 (Build)
# ---------------------------------------------------
FROM node:18-slim AS build

WORKDIR /app

# 复制 vite-project 内的依赖文件
COPY vite-project/package*.json ./vite-project/

# 进入 vite-project 安装依赖
RUN cd vite-project && npm ci

# 复制整个项目
COPY . .

# 进入 vite-project 构建
RUN cd vite-project && npm run build

# ---------------------------------------------------
# 阶段 2: 运行 (Run)
# ---------------------------------------------------
FROM nginx:1.25-alpine

# 复制 nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制构建产物
COPY --from=build /app/vite-project/dist /usr/share/nginx/html

# 暴露端口
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
