# ---------------------------------------------------
# 阶段 1: 构建 (Build) - 使用一个包含完整JDK和Maven的镜像来编译我们的Java代码
# ---------------------------------------------------
# 使用官方的 Maven 镜像，它基于 Java 17 (与Spring Boot 3兼容)
FROM maven:3.9.6-eclipse-temurin-17-focal AS build

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 和 src 目录到容器中
# (我们只复制这两个，以充分利用Docker的层缓存)
COPY pom.xml .
COPY src ./src

# 运行 Maven 命令来打包项目，跳过测试以加快速度
# 这会在容器的 /app/target/ 目录下生成 .jar 文件
RUN mvn clean package -DskipTests

# ---------------------------------------------------
# 阶段 2: 运行 (Run) - 使用一个轻量级的JRE镜像来运行我们的应用
# ---------------------------------------------------
# 使用一个非常小的、仅包含Java 17运行环境的镜像
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 【关键】从 "build" 阶段复制我们打包好的 .jar 文件
# 我们假设它叫 datingapp-0.0.1-SNAPSHOT.jar。
# **请您务必检查一下您本地 'target' 目录下的 .jar 文件全名，并在这里修改它！**
COPY --from=build /app/target/Love-0.0.1-SNAPSHOT.jar app.jar

# 暴露后端服务运行的 8080 端口
EXPOSE 8080

# 容器启动时运行的命令：使用 java -jar 来启动我们的应用
CMD ["java", "-jar", "app.jar"]