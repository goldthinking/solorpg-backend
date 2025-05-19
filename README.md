# solorpg
## 一、产品设计

AI 辅助单人剧本杀是一款创新型的线上推理游戏平台，核心目标是：

- **沉浸式单人体验**：玩家无需组队，即可与 AI 驱动的虚拟角色互动，体验传统多人剧本杀的乐趣。
- **智能角色交互**：AI 角色通过自然语言理解与生成，回答玩家提问，推动剧情发展。
- **线索搜集与推理**：玩家在虚拟场景中搜集线索、记录行动日志，通过逻辑推理揭开谜团。
- **评分与反馈**：AI 自动对玩家的解谜表现进行量化评分，并生成文字化反馈，帮助玩家复盘。

------

## 二、技术架构

```
┌───────────────┐      ┌───────────────────────────┐
│   前   端      │      │      后    端             │
│ uni-app       │◀────▶│ Spring Boot + MyBatis-Plus│
│ (多端小程序)   │      │ + Python LLM 服务         │
└───────────────┘      └───────────────────────────┘
         │                     │
         │ HTTP/REST + WebSocket│
         ▼                     ▼
    AI 交互服务         游戏逻辑分支管理系统
    • NLP 意图识别      • 剧本状态机
    • 文本生成          • 分支条件、存档、回溯
```

- **前端**：基于 uni-app，实现微信/Web/APP 多端适配，负责 UI 渲染与用户交互。
- **后端**：
  - **主框架**：Spring Boot + MyBatis-Plus（CRUD、代码生成、事务管理）
  - **LLM 服务**：Python Flask/FastAPI 托管各类大语言模型接口（本地/远程调用）
- **AI 交互**：自然语言处理与生成，层面涵盖分词、意图分类、对话维护、答案生成。
- **游戏逻辑**：自研剧情分支管理系统，根据玩家提问与行动动态更新剧情节点，实现“存档—分支—回溯”机制。

------

## 三、进度汇报

### 1. 数据库设计

- 已完成核心表结构搭建，采用 MySQL，Navicat Premium 导出如下：

  ![image-20250426112936604](C:\Users\Windows 10\AppData\Roaming\Typora\typora-user-images\image-20250426112936604.png)

### 2. Spring Boot 项目骨架

- 使用 MyBatis-Plus CodeGenerator 自动生成实体、Mapper、Service、Controller 模板

```java
public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://124.220.94.212:3306/solorpg?serverTimezone=GMT%2B8", "Nemotte", "Jinhuahua_312")
				// 全局配置
				.globalConfig(builder -> {
					builder.author("Nemotte") // 设置作者
							.outputDir("C:\\Nemotte\\Java\\solorpg-backend\\src\\main\\java");}) // 指定输出目录
				// 包配置
				.packageConfig(builder ->
						builder.parent("com.solorpgbackend") // 设置父包名
								.moduleName("") // 设置父包模块名
								.pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Nemotte\\Java\\solorpg-backend\\src\\main\\resources\\mapper")) // 设置mapperXml生成路径
				)
				// 策略配置
				.strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
						.entityBuilder()
						.enableLombok()
						.addTableFills(
								new Column("create_time", FieldFill.INSERT)
						)
						.build())
				// 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.templateEngine(new FreemarkerTemplateEngine())
				.execute();
	}

	// 处理 all 情况
	protected static List<String> getTables(String tables) {
		return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
	}
```

### 3. 服务器环境与容器化部署

- **前后端镜像打包**：
  - 前端：uni-app 编译产出静态资源，基于 Nginx 镜像打包
  - 后端：多阶段 Maven 构建 → OpenJDK 运行时镜像
- **容器编排**：使用 Docker Compose 管理 `solorpg-frontend`、`solorpg-backend`、`solorpg-github-runner` 三个服务
- **效果展示**：[SOLORPG-DEMO](http://124.220.94.212:5173/)

### 4. GitHub Actions CI/CD

- Workflow 触发：
  - `push` / `pull_request` 针对 `main` 分支均触发
- 步骤：
  1. Checkout 代码 → 编译前端 & 后端
  2. Docker build & push（至私有 Registry）
  3. SSH 部署：拉取新镜像 → 停止旧容器 → 启动新容器

```yml
name: Build and Deploy

on:
  push:
    branches:
      - main  # 仅在推送到 main 分支时触发
  pull_request:
    branches:
      - main

jobs:
  build:
    name: Build on remote server
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v3

      - name: Set up SSH key
        run: |
          mkdir -p ~/.ssh
          echo "${{ secrets.SSH_PRIVATE_KEY }}" > ~/.ssh/id_rsa
          chmod 600 ~/.ssh/id_rsa
          ssh-keyscan -H 124.220.94.212 >> ~/.ssh/known_hosts

      - name: Build project on remote server
        run: |
          ssh ubuntu@124.220.94.212 '
            export PATH=/home/ubuntu/.nvm/versions/node/v18.20.8/bin:$PATH &&
            cd /home/ubuntu/services/solorpg/solorpg-proto &&
            git pull &&
            npm install --legacy-peer-deps &&
            npm run build &&
            docker-compose -f /home/ubuntu/services/solorpg/docker-compose.yml up -d &&
            docker-compose -f /home/ubuntu/services/solorpg/docker-compose.yml down &&
            docker-compose -f /home/ubuntu/services/solorpg/docker-compose.yml rm solorpg-frontend &&
            docker rmi solorpg-frontend &&
            docker build -t solorpg-frontend .
          '

  deploy:
    name: Deploy on remote server
    runs-on: ubuntu-latest
    needs: build

    steps:
      - name: Set up SSH key
        run: |
          mkdir -p ~/.ssh
          echo "${{ secrets.SSH_PRIVATE_KEY }}" > ~/.ssh/id_rsa
          chmod 600 ~/.ssh/id_rsa
          ssh-keyscan -H 124.220.94.212 >> ~/.ssh/known_hosts

      - name: Deploy application
        run: |
          ssh ubuntu@124.220.94.212 '
            docker-compose -f /home/ubuntu/services/solorpg/docker-compose.yml up -d
          '
```

- 实现全自动化，无需人工介入。

### 5. LLM 效果比对

- **剧本数据**：
  - XBB 源剧本（不完整，效果欠佳）
  - 自建完整版剧本（覆盖所有逻辑分支）
  
- **模型测试**：
  - 国内：
  - - 豆包（Doubao）
  
  ![image-20250426120654882](C:\Users\Windows 10\AppData\Roaming\Typora\typora-user-images\image-20250426120654882.png)
  
  - - Deepseek V3
  
      ![image-20250426120807758](C:\Users\Windows 10\AppData\Roaming\Typora\typora-user-images\image-20250426120807758.png)
  
  - 国外：OpenAI GPT、Grok
  
  ![image-20250426120831140](C:\Users\Windows 10\AppData\Roaming\Typora\typora-user-images\image-20250426120831140.png)
  
- **对比结论**：
  
  - DeepSeek v3 在回答准确性、对话连贯度上略优于 Grok
  - 各模型普遍存在“剧透”倾向，尤其在细节问答中
  - Turtle-Soup（海龟汤）剧本整体可用，但推理问答中易提前泄露关键信息
  
- **后续计划**：
  - 优化 Prompt，引入“**禁止剧透**”策略词
  - 增加对话上下文约束，动态屏蔽未达成条件的关键信息
  - 持续收集用户测试数据，微调 NLI/NLG 模型

------

> **下一步**：
>
> - 完成 Prompt 调整，减少剧透风险
> - 前端 UI/UX 微调
> - 接口设计和实现
