# 使用带 Java 17 的 Maven 镜像作为构建阶段
FROM maven:3.6.3-openjdk-17-slim AS build

# 设置工作目录
WORKDIR /app

# 复制项目文件到 Docker 镜像中
COPY . .

# 使用更轻量级的 JDK 17 镜像来运行应用
FROM openjdk:17-slim

# 设置工作目录
WORKDIR /app

# 复制构建阶段生成的 JAR 文件到新的镜像中
COPY --from=build /app/target/*.jar /app/app.jar

# 暴露应用程序端口（假设你的应用监听在 8080 端口）
EXPOSE 8848

# 设置容器启动时的命令来运行 Java 应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
