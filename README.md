# TeamTaskApp

TeamTaskApp 是一个基于 Jetpack Compose 的团队任务协作原型项目，用于练习现代 Android 企业级开发中的页面结构、Material 3 主题、Navigation、UI State、表单校验和异常状态处理。

本项目当前阶段不接入真实后端，先使用本地 Fake 数据源模拟企业任务协作场景，保证在没有接口的情况下也能完整推进 UI 开发。

## 技术栈

* Kotlin
* Jetpack Compose
* Material 3
* AndroidX
* Compose Navigation
* Fake Repository
* Git / GitHub

## 项目目标

本项目以“团队任务协作”为业务背景，逐步完成一个具备企业项目雏形的 Android App。

核心页面包括：

* 首页：展示任务列表
* 详情页：展示任务标题、描述、状态、负责人、截止日期
* 创建页：通过表单创建任务
* 个人页：展示用户信息和设置入口

核心 UI 状态包括：

* Loading：加载中
* Success：正常数据
* Empty：空数据
* Error：加载失败或权限不足

## 当前目录结构

```text
TeamTaskApp
├── app
│   └── src
│       └── main
│           └── java
│               └── com/nep/teamtask
│                   ├── data
│                   │   ├── fake
│                   │   │   └── FakeTaskRepository.kt
│                   │   └── model
│                   │       ├── Task.kt
│                   │       ├── TaskStatus.kt
│                   │       └── User.kt
│                   ├── ui
│                   │   ├── component
│                   │   │   ├── LoadingView.kt
│                   │   │   ├── EmptyView.kt
│                   │   │   ├── ErrorView.kt
│                   │   │   └── AppPlaceholder.kt
│                   │   ├── navigation
│                   │   │   ├── TeamTaskRoutes.kt
│                   │   │   ├── TeamTaskNavHost.kt
│                   │   │   ├── TeamTaskBottomBar.kt
│                   │   │   └── TeamTaskBottomNavItem.kt
│                   │   ├── screen
│                   │   │   ├── HomeScreen.kt
│                   │   │   ├── TaskDetailScreen.kt
│                   │   │   ├── CreateTaskScreen.kt
│                   │   │   └── ProfileScreen.kt
│                   │   ├── state
│                   │   │   └── UiState.kt
│                   │   └── theme
│                   │       ├── Color.kt
│                   │       ├── Theme.kt
│                   │       └── Type.kt
│                   └── MainActivity.kt
├── docs
│   ├── phase-01-project-setup.md
│   ├── phase-02-project-setup.md
│   ├── phase-03-project-setup.md
│   └── phase-04-project-setup.md
└── README.md
```

## 已完成内容

### Phase 01：项目基础搭建

* 创建 Jetpack Compose 项目
* 拆分基础包结构
* 配置 Material 3 主题
* 理解 `setContent`、`MaterialTheme`、`Surface` 和 Compose 项目组织方式

### Phase 02：数据模型与 Fake 数据源

* 定义 `User` 数据模型
* 定义 `TaskStatus` 枚举：`TODO`、`IN_PROGRESS`、`DONE`、`BLOCKED`
* 定义 `Task` 数据模型
* 编写 `FakeTaskRepository`
* 支持正常数据、空数据、加载失败、权限不足等模拟场景
* 为后续 Loading / Empty / Error / Success 状态展示做准备

### Phase 03：通用 UI 状态与组件

* 定义 `UiState<T>` 密封接口
* 支持 `Loading`、`Success`、`Empty`、`Error` 四种页面状态
* 编写 `LoadingView`
* 编写 `EmptyView`
* 编写 `ErrorView`
* 编写 `AppPlaceholder`
* 建立“页面不能只考虑成功状态”的企业级 UI 思维

### Phase 04：页面导航 Navigation

* 引入 Compose Navigation
* 定义集中式路由对象 `TeamTaskRoutes`
* 设计首页、详情页、创建页、个人页四类路由
* 支持详情页 `taskId` 参数传递
* 编写 `TeamTaskNavHost`
* 使用 `NavHost` 和 `composable` 组织页面导航图
* 使用 `navController.navigate()` 实现页面跳转
* 使用 `popBackStack()` 实现返回
* 使用 `Scaffold` 承载底部导航栏
* 使用 `NavigationBar` 和 `NavigationBarItem` 实现底部 Tab
* 底部导航栏包含 `Tasks` 和 `Profile` 两个一级页面
* 详情页和创建页作为二级页面，不显示底部导航栏

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

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    BLOCKED
}

data class User(
    val id: String,
    val name: String,
    val role: String,
    val avatarUrl: String? = null
)
```

## 页面导航结构

```text
TeamTaskRoutes
├── HOME
│   └── 首页 / 任务列表
├── TASK_DETAIL
│   └── 详情页 / task_detail/{taskId}
├── CREATE_TASK
│   └── 创建任务页
└── PROFILE
    └── 个人页
```

页面跳转关系：

```text
HomeScreen
├── 点击任务 -> TaskDetailScreen(taskId)
├── 点击创建 -> CreateTaskScreen
└── 底部 Profile Tab -> ProfileScreen

ProfileScreen
└── 底部 Tasks Tab -> HomeScreen

TaskDetailScreen
└── 返回 -> 上一页

CreateTaskScreen
└── 创建成功 / 返回 -> 上一页
```

## 底部导航栏设计

底部导航栏只承载一级页面：

| Tab     | 页面  | 路由        |
| ------- | --- | --------- |
| Tasks   | 首页  | `home`    |
| Profile | 个人页 | `profile` |

详情页和创建页属于二级页面，不放入底部导航栏。

| 页面  | 是否显示底部栏 | 原因           |
| --- | ------: | ------------ |
| 首页  |       是 | 一级页面         |
| 个人页 |       是 | 一级页面         |
| 详情页 |       否 | 从任务列表进入的二级页面 |
| 创建页 |       否 | 独立创建流程页面     |

## Fake 数据源能力

`FakeTaskRepository` 当前支持以下场景：

| 场景              | 说明       |
| --------------- | -------- |
| `NORMAL`        | 正常返回示例任务 |
| `EMPTY`         | 返回空任务列表  |
| `LOAD_FAILED`   | 模拟网络加载失败 |
| `NO_PERMISSION` | 模拟权限不足   |

所有异步接口统一返回 `FakeTaskResult<T>`，调用方需要按 `Success`、`Failure`、`NoPermission` 分支处理。

这样做的好处是：即使后端接口还没有开发完成，也可以先完整推进 UI 页面、状态管理和交互逻辑。

## 当前阶段验收结果

Phase 04 完成后，项目应满足：

* App 启动后默认进入任务首页
* 首页和个人页可以通过底部 Tab 切换
* 当前 Tab 具备选中状态
* 首页可以跳转到详情页
* 详情页可以接收 `taskId` 参数
* 首页可以跳转到创建页
* 详情页和创建页不显示底部导航栏
* 返回按钮或系统返回可以回到上一页
* 页面跳转逻辑集中在 navigation 层，不散落在各个页面内部

## 后续计划

* 完善 `HomeScreen`：接入 `FakeTaskRepository`
* 根据 `UiState` 切换 Loading / Empty / Error / Success 视图
* 编写任务列表项组件：`TaskCard`
* 完善 `TaskDetailScreen`
* 编写 `CreateTaskScreen` 表单
* 增加表单校验
* 增加 Snackbar 操作反馈
* 增加 Dialog 确认流程
* 完善 `ProfileScreen`
* 继续统一页面容器、顶部栏和状态组件

## 文档目录

| 文档                               | 说明             |
| -------------------------------- | -------------- |
| `docs/phase-01-project-setup.md` | 项目基础搭建         |
| `docs/phase-02-project-setup.md` | 数据模型与 Fake 数据源 |
| `docs/phase-03-project-setup.md` | 通用 UI 状态与组件    |
| `docs/phase-04-project-setup.md` | 页面导航与底部导航栏     |

## 项目定位

TeamTaskApp 不是一个只追求页面能显示的 Demo，而是一个用于练习企业级 Android 开发思维的阶段性项目。

当前重点包括：

* 清晰的项目分层
* 可维护的主题体系
* 可复用的 UI 状态组件
* 集中式路由管理
* 一级页面和二级页面的导航分层
* 不依赖真实后端的 Fake 数据开发方式
* 对 Loading / Empty / Error / Success 状态的完整考虑
