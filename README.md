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

本项目以“团队任务协作”为业务背景，逐步完成一个具备企业项目雏形的 Android App。

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
- No Permission：权限不足

## 当前目录结构

```text
TeamTaskApp
├── data
│   ├── fake
│   │   └── FakeTaskRepository.kt
│   └── model
│       ├── Task.kt
│       ├── TaskStatus.kt
│       └── User.kt
├── ui
│   ├── component
│   ├── navigation
│   ├── screen
│   └── theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt
```



## 已完成内容
### Phase 01：项目基础搭建
- 创建 Jetpack Compose 项目
- 拆分基础包结构
- 配置 ui/theme
- 配置 Material 3 主题
- 理解 setContent
- 理解 MaterialTheme
- 理解基础 Compose 项目组织方式

### Phase 02：数据模型与 Fake 数据源
已完成：
- 定义 User 数据模型
- 定义 TaskStatus 枚举
- 定义 Task 数据模型
- 编写 FakeTaskRepository
- 模拟正常数据、空数据、加载失败、权限不足
- 为后续 loading / empty / error / no permission UI 状态做准备




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

FakeTaskRepository 当前支持以下场景：

|场景|说明|
|---|---|
|`NORMAL`|正常返回任务列表|
|`EMPTY`|返回空任务列表|
|`LOAD_FAILED`|模拟加载失败|
|`NO_PERMISSION`|模拟权限不足|


这样做的目的是：即使后端接口还没有开发完成，也可以先完整推进 UI 页面、状态管理和交互逻辑。




## 后续计划

下一步进入 Compose 页面状态设计与页面开发：

- 定义首页 UI State
- 编写任务列表页
- 编写 Loading UI
- 编写 Empty UI
- 编写 Error UI
- 编写 No Permission UI
- 接入 FakeTaskRepository
- 后续再引入 Navigation 和创建任务页面