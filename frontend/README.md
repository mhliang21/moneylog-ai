<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://github.com/user-attachments/assets/0aa67016-6eaf-458a-adb2-6e31a0763ed6" />
</div>

# MoneyLog AI

本项目包含：
- React + Vite 前端应用
- Java Spring Boot 后端服务

## 目录结构

- `backend/`：后端代码（Spring Boot，MySQL，Gemini API 调用）
- 根目录其它文件：前端代码（React + Vite）

前端主要文件：
- `App.tsx`、`index.tsx`、`index.html`
- `components/`、`services/`、`types.ts`
- `package.json`、`vite.config.ts`、`tsconfig.json`

后端主要文件：
- `backend/pom.xml`
- `backend/src/main/java/com/moneylog/ai/**`
- `backend/src/main/resources/application.properties`

## 启动前端

**前置条件：** Node.js 18+

```bash
npm install
npm run dev
```

## 启动后端

详细说明见 `backend/README.md` 或 `backend/QUICKSTART.md`，这里是简要步骤：

```bash
cd backend
mvn spring-boot:run
```

前端默认运行在 `http://localhost:3000` 或 `http://localhost:5173`，后端在 `http://localhost:8080`。
