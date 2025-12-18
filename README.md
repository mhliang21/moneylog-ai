# MoneyLog AI Backend

Spring Boot 后端服务，为 MoneyLog AI 前端应用提供 API 支持。

## 功能特性

- ✅ **API Key 保护**：Gemini API Key 仅在后端配置，不会暴露给前端
- ✅ **数据持久化**：使用 H2 数据库（开发环境），可轻松切换为 MySQL（生产环境）
- ✅ **完整的 CRUD 接口**：
  - 获取资产组合（GET）
  - 保存资产组合（POST）
  - 添加资产（POST）
  - 更新资产（PUT）
  - 删除资产（DELETE）
  - 生成 AI 摘要（POST）
  - 获取历史记录（GET）
- ✅ **CORS 配置**：支持前端跨域调用
- ✅ **AI 摘要生成**：集成 Gemini API，自动生成月度财务点评

## 技术栈

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (开发环境)
- MySQL (生产环境)
- WebFlux (用于调用 Gemini API)
- Lombok

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+

### 2. 配置 API Key

**重要：** Gemini API Key 现在在后端配置，不会暴露给前端，提高了安全性。

配置方式（任选其一）：

**方式 1：环境变量（推荐）**
```bash
# Windows
set GEMINI_API_KEY=your-api-key-here

# Linux/Mac
export GEMINI_API_KEY=your-api-key-here
```

**方式 2：直接修改 application.properties**
```properties
gemini.api.key=your-api-key-here
```

**注意：** 如果未配置 API Key，AI 摘要功能将无法使用，但其他功能（增删改查）仍然正常。

### 3. 运行项目

```bash
cd backend
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动。

### 4. 访问 H2 控制台（开发环境）

访问 `http://localhost:8080/h2-console` 查看数据库：
- JDBC URL: `jdbc:h2:file:./data/moneylog`
- Username: `sa`
- Password: (留空)

## API 接口文档

所有接口的基础路径：`http://localhost:8080/api`

### 1. 获取指定月份的资产组合
```http
GET /api/portfolios/{date}
```
**参数：** `date` - 日期格式：YYYY-MM（例如：2025-01）

**响应示例：**
```json
{
  "id": 1,
  "date": "2025-01",
  "positions": [
    {
      "id": 1,
      "name": "基金名称",
      "category": "AH_Stock",
      "amount": 99000.0,
      "monthlyGain": 475.95,
      "totalGain": 4200.0
    }
  ],
  "aiSummary": "AI 生成的摘要..."
}
```

### 2. 保存资产组合
```http
POST /api/portfolios
Content-Type: application/json
```
**请求体：**
```json
{
  "date": "2025-01",
  "positions": [
    {
      "name": "基金名称",
      "category": "AH_Stock",
      "amount": 99000.0,
      "monthlyGain": 475.95,
      "totalGain": 4200.0
    }
  ],
  "aiSummary": "可选：AI 摘要"
}
```

### 3. 添加资产
```http
POST /api/portfolios/{date}/positions
Content-Type: application/json
```
**请求体：**
```json
{
  "name": "基金名称",
  "category": "AH_Stock",
  "amount": 99000.0,
  "monthlyGain": 475.95,
  "totalGain": 4200.0
}
```

**支持的分类（category）：**
- `AH_Stock` - A/H股基金
- `US_Stock` - 美股基金
- `Commodity` - 商品
- `Bond` - 债券基金
- `Wealth` - 理财
- `Cash` - 活钱

### 4. 更新资产
```http
PUT /api/portfolios/{date}/positions/{positionId}
Content-Type: application/json
```
**请求体：** 同添加资产的请求体

### 5. 删除资产
```http
DELETE /api/portfolios/{date}/positions/{positionId}
```

### 6. 生成 AI 摘要
```http
POST /api/portfolios/{date}/ai-summary
```
**说明：** 根据该月份的资产组合数据，调用 Gemini API 生成财务点评摘要。

**响应：** 纯文本字符串

### 7. 获取历史记录
```http
GET /api/portfolios/history
```
**响应示例：**
```json
[
  {
    "month": "2025-01",
    "totalAssets": 990000.0,
    "totalGain": 4759.5
  },
  {
    "month": "2025-02",
    "totalAssets": 995000.0,
    "totalGain": 5000.0
  }
]
```

## 数据库切换

### 切换到 MySQL

1. 修改 `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moneylog
spring.datasource.username=root
spring.datasource.password=your-password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

2. 创建数据库:
```sql
CREATE DATABASE moneylog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/moneylog/ai/
│   │   │   ├── entity/          # 实体类
│   │   │   ├── repository/      # 数据访问层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── controller/      # 控制器层
│   │   │   ├── dto/             # 数据传输对象
│   │   │   └── config/          # 配置类
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

