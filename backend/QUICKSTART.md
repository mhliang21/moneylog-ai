# 快速启动指南

## 前置要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- Gemini API Key（可选，用于 AI 摘要功能）

## 启动步骤

### 1. 配置 Gemini API Key（可选）

如果不需要 AI 摘要功能，可以跳过此步骤。

**方式 1：环境变量（推荐）**
```bash
# Windows
set GEMINI_API_KEY=your-api-key-here

# Linux/Mac
export GEMINI_API_KEY=your-api-key-here
```

**方式 2：修改配置文件**
编辑 `src/main/resources/application.properties`：
```properties
gemini.api.key=your-api-key-here
```

### 2. 启动后端服务

```bash
cd backend
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动。

### 3. 验证服务运行

打开浏览器访问：
- API 健康检查：`http://localhost:8080/api/portfolios/2025-01`
- H2 数据库控制台：`http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:./data/moneylog`
  - Username: `sa`
  - Password: (留空)

### 4. 启动前端（另一个终端）

```bash
# 在项目根目录
npm install
npm run dev
```

前端将在 `http://localhost:5173` 启动（或 Vite 配置的端口）。

## 常见问题

### 1. 端口冲突

如果 8080 端口被占用，修改 `application.properties`：
```properties
server.port=8081
```

同时更新前端的 `apiService.ts` 中的 `API_BASE_URL`。

### 2. CORS 错误

如果前端无法访问后端 API，检查：
- 后端 `application.properties` 中的 `spring.web.cors.allowed-origins` 是否包含前端地址
- 前端 `apiService.ts` 中的 `API_BASE_URL` 是否正确

### 3. 数据库文件位置

H2 数据库文件默认保存在 `backend/data/moneylog.mv.db`。

### 4. AI 摘要功能不工作

- 检查 API Key 是否正确配置
- 检查网络连接（需要访问 Google API）
- 查看后端日志中的错误信息

## 生产环境部署

### 切换到 MySQL

1. 修改 `application.properties`：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moneylog
spring.datasource.username=root
spring.datasource.password=your-password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

2. 创建数据库：
```sql
CREATE DATABASE moneylog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 重新启动服务，数据库表将自动创建。

### 打包部署

```bash
mvn clean package
java -jar target/moneylog-ai-backend-1.0.0.jar
```

## 项目结构说明

```
backend/
├── src/main/java/com/moneylog/ai/
│   ├── entity/          # 数据库实体类
│   │   ├── Portfolio.java
│   │   ├── AssetPosition.java
│   │   └── AssetCategory.java
│   ├── repository/      # 数据访问层
│   │   ├── PortfolioRepository.java
│   │   └── AssetPositionRepository.java
│   ├── service/         # 业务逻辑层
│   │   ├── PortfolioService.java
│   │   └── GeminiService.java
│   ├── controller/      # REST API 控制器
│   │   └── PortfolioController.java
│   ├── dto/             # 数据传输对象
│   │   ├── PortfolioDTO.java
│   │   ├── AssetPositionDTO.java
│   │   ├── HistoryRecordDTO.java
│   │   ├── GeminiRequest.java
│   │   └── GeminiResponse.java
│   ├── config/          # 配置类
│   │   └── CorsConfig.java
│   └── MoneyLogAiApplication.java  # 主应用类
└── src/main/resources/
    └── application.properties      # 配置文件
```

## 下一步

- 查看 [README.md](README.md) 了解详细的 API 文档
- 查看前端代码了解如何使用这些 API
- 根据需要自定义业务逻辑和数据库配置

