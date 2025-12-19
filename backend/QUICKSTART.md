# 快速启动指南

## 前置要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- MySQL（本地开发）或相应的数据库（用于持久化数据）
- Gemini API Key（可选，用于 AI 摘要功能）

## 启动步骤

### 1. 配置 Gemini API Key（可选）

如果不需要 AI 摘要功能，可以跳过此步骤。

**方式 1：环境变量（推荐）**
```powershell
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

```powershell
cd backend
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动（默认）。

### 3. 验证服务运行与数据库数据

后端默认连接到本地 MySQL（见 `src/main/resources/application.properties`）：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moneylog?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=123456
```

如果你使用 MySQL，请确保数据库 `moneylog` 已创建并且 MySQL 服务在 `localhost:3306` 上运行。

快速检查（PowerShell）：
```powershell
# 检查端口
Test-NetConnection -ComputerName localhost -Port 3306

# 检查 Java 和 Maven
java -version
mvn -v
```

验证应用启动后，使用 API 验证是否有初始化数据：
```powershell
# 获取历史汇总
Invoke-RestMethod -Uri 'http://localhost:8080/api/historical-assets/history' -Method GET

# 获取当月资产明细（格式 YYYY-MM）
$month = (Get-Date).ToString('yyyy-MM')
Invoke-RestMethod -Uri "http://localhost:8080/api/historical-assets/$month" -Method GET
```

如果你想直接在数据库中查看表记录（MySQL）：
```sql
USE moneylog;
SELECT COUNT(*) FROM asset_positions;
SELECT COUNT(*) FROM historical_asset_records;
SELECT * FROM historical_asset_records LIMIT 10;
```

### 4. 启动前端（另一个终端）

```powershell
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

同时更新前端的 `apiService.ts` 中的 `API_BASE_URL`（或使用环境变量 `VITE_API_BASE_URL`）。

### 2. CORS 错误

如果前端无法访问后端 API，检查：
- 后端 `application.properties` 中的 `spring.web.cors.allowed-origins` 是否包含前端地址
- 前端 `apiService.ts` 中的 `API_BASE_URL` 是否正确

### 3. 旧的 H2 数据文件（可选）

本仓库历史上曾使用 H2 文件（`backend/data/moneylog.mv.db`）。当前项目默认使用 MySQL。若你不需要该 H2 文件，可以手动删除：
```powershell
Remove-Item -Path .\backend\data\moneylog.mv.db -Force
```

或者保留它作为本地备份。

### 4. AI 摘要功能不工作

- 检查 API Key 是否正确配置
- 检查网络连接（需要访问 Google API）
- 查看后端日志中的错误信息

## 生产环境部署

### 切换到 MySQL（已为默认）

1. 修改 `application.properties`（如果不同）
2. 创建数据库：
```sql
CREATE DATABASE moneylog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 重新启动服务，数据库表将自动创建。

### 打包部署

```powershell
mvn clean package
java -jar target/moneylog-ai-backend-1.0.0.jar
```

## 项目结构说明

参见代码仓库。
