# Node.js 更新指南

## 当前状态
- 当前 Node.js 版本：v16.15.1
- 推荐版本：v20.x LTS 或 v22.x LTS

## 更新方法

### 方法 1：官方安装程序（推荐）

1. **访问 Node.js 官网**
   - 网址：https://nodejs.org/zh-cn/
   - 下载 Windows Installer (.msi) 的 LTS 版本

2. **运行安装程序**
   - 双击下载的 `.msi` 文件
   - 按照安装向导完成安装
   - 建议选择默认选项（会自动添加到 PATH）

3. **验证安装**
   ```bash
   node --version
   npm --version
   ```

4. **重启终端**
   - 关闭所有终端窗口
   - 重新打开新的终端窗口
   - 确保新版本生效

### 方法 2：使用 nvm-windows（管理多个版本）

1. **下载 nvm-windows**
   - 访问：https://github.com/coreybutler/nvm-windows/releases
   - 下载 `nvm-setup.exe`

2. **安装 nvm-windows**
   - 运行安装程序
   - 完成后重启终端

3. **安装最新 LTS 版本**
   ```bash
   nvm install lts
   nvm use lts
   ```

4. **验证**
   ```bash
   node --version
   ```

## 注意事项

- 更新 Node.js 后，可能需要重新安装项目依赖：
  ```bash
  npm install
  ```

- 如果遇到权限问题，请以管理员身份运行终端

- 建议使用 LTS（长期支持）版本，更稳定可靠

## 项目要求

本项目推荐使用 Node.js 18+ 版本，因为：
- React 19 和 Vite 6 需要较新的 Node.js 版本
- 更好的性能和安全性
- 支持最新的 JavaScript 特性



