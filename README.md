# TeamTaskApp

TeamTaskApp 是一个基于 Jetpack Compose 的团队任务协作原型项目，用于练习现代 Android 企业级开发中的页面结构、Material 3 主题、Navigation、UI State、表单校验和异常状态处理。

本项目当前阶段不接入真实后端，先使用本地 Fake 数据源模拟企业任务协作场景，保证在没有接口的情况下也能完整推进 UI 开发。

## 技术栈

- Kotlin
- Jetpack Compose
- Material 3
- AndroidX
- Compose Navigation
- Fake Repository
- Git / GitHub

## 项目目标

本项目以"团队任务协作"为业务背景，逐步完成一个具备企业项目雏形的 Android App。

核心页面包括：

- 首页：任务列表
- 详情页：任务标题、描述、状态、负责人、截止日期
- 创建页：表单创建任务
- 个人页：用户信息、设置入口

核心 UI 状态包括：

- Loading：加载中
- Content：正常数据
- Empty：空数据
- Error：加载失败

## 当前目录结构

```text
TeamTaskApp
├── data
│   ├── fake
│   │   └── FakeTaskRepository.kt          # 模拟数据仓库
│   └── model
│       ├── Task.kt                         # 任务数据模型
│       ├── TaskStatus.kt                   # 任务状态枚举
│       └── User.kt                         # 用户数据模型
├── ui
│   ├── component
│   │   ├── LoadingView.kt                  # 全屏加载占位组件
│   │   ├── EmptyView.kt                    # 空状态占位组件
│   │   ├── ErrorView.kt                    # 错误状态占位组件
│   │   └── AppPlaceholder.kt              # 通用占位组件
│   ├── navigation
│   │   └── TeamTaskRoutes.kt              # 路由常量定义
│   ├── screen
│   │   └── HomeScreen.kt                   # 首页（占位）
│   ├── state
│   │   └── UiState.kt                      # 通用 UI 状态封装
│   └── theme
│       ├── Color.kt                        # 品牌色 & 语义色
│       ├── Theme.kt                        # Material 3 主题 & 自定义间距系统
│       └── Type.kt                         # 自定义字体排版
└── MainActivity.kt                         # 应用入口
```

## 已完成内容

### Phase 01：项目基础搭建

- 创建 Jetpack Compose 项目
- 拆分基础包结构
- 配置 Material 3 主题
- 理解 setContent / MaterialTheme / Compose 项目组织方式

### Phase 02：数据模型与 Fake 数据源

- 定义 `User` 数据模型
- 定义 `TaskStatus` 枚举（TODO / IN_PROGRESS / DONE / BLOCKED）
- 定义 `Task` 数据模型
- 编写 `FakeTaskRepository`，内置 5 条示例任务和 4 名团队成员
- 定义 `FakeTaskScenario` 枚举，支持 NORMAL / EMPTY / LOAD_FAILED / NO_PERMISSION 四种场景切换
- 定义 `FakeTaskResult<T>` 密封接口（Success / Failure / NoPermission）
- 实现 `getTasks()` / `getTaskById()` / `createTask()` 挂起函数，均带 800ms 模拟延迟

### Phase 03：Material 3 主题体系

- 定义完整的品牌色板（Brand / Neutral / Semantic），支持亮色与暗色双主题
- 定义 `TeamTaskShapes` 统一圆角规范（6dp ~ 24dp 五档）
- 定义 `TeamTaskSpacing` 自定义间距系统（extraSmall ~ contentPadding 七档），通过 `TeamTaskTheme.spacing` 全局访问
- 引入三套自定义字体：
  - `JetBrainsMono` — displayLarge / headlineLarge
  - `DejaVuSansMonoNerdFont` — headlineMedium
  - `JasonHandwriting` — bodyMedium / bodyLarge / labelMedium
- 支持 Android 12+ 动态取色（dynamic color）

### Phase 04：通用 UI 状态与组件

- 定义 `UiState<T>` 密封接口：Loading / Success / Empty / Error，覆盖所有常见页面状态
- 设计 `LoadingView`：居中 CircularProgressIndicator + 可配置提示文本
- 设计 `EmptyView`：图标 + 标题 + 描述 + 可选操作按钮
- 设计 `ErrorView`：警告图标 + 错误信息 + 重试按钮（必传回调）
- 设计 `AppPlaceholder`：通用文本占位组件
- 定义 `TeamTaskRoutes` 路由常量：HOME / TASK_DETAIL / CREATE_TASK / PROFILE
- 编写 `HomeScreen` 占位页面，接入自定义主题

## 核心数据模型

```kotlin
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val assignee: User,
    val dueDate: String
)
```

```kotlin
enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    BLOCKED
}
```

```kotlin
data class User(
    val id: String,
    val name: String,
    val role: String,
    val avatarUrl: String? = null
)
```

## Fake 数据源能力

`FakeTaskRepository` 当前支持以下场景：

| 场景 | 说明 |
|---|---|
| `NORMAL` | 正常返回 5 条示例任务 |
| `EMPTY` | 返回空任务列表 |
| `LOAD_FAILED` | 模拟网络加载失败 |
| `NO_PERMISSION` | 模拟权限不足 |

所有异步接口统一返回 `FakeTaskResult<T>`，调用方需按 `Success` / `Failure` / `NoPermission` 分支处理。

这样做的好处是：即使后端接口还没有开发完成，也可以先完整推进 UI 页面、状态管理和交互逻辑。

## 后续计划

- 完善 `HomeScreen`：接入 `FakeTaskRepository`，根据 `UiState` 切换 Loading / Empty / Error / Content 视图
- 编写任务列表项组件（TaskCard / TaskListItem）
- 实现 Compose Navigation 页面路由与参数传递
- 编写任务详情页（TaskDetailScreen）
- 编写任务创建页（CreateTaskScreen），含表单校验
- 编写个人中心页（ProfileScreen）
