# Phase 1：项目基础搭建

## 阶段目标

本阶段目标是完成 TeamTaskApp 的基础工程结构，为后续页面开发、导航、组件抽取和状态处理打好基础。

重点不是实现复杂业务，而是建立清晰、可扩展、可维护的 Compose 项目结构。

## 完成内容

### 1. 创建 Compose 项目

已创建一个干净的 Jetpack Compose 项目，项目入口为：

```text
MainActivity.kt
```

入口结构：

```text
MainActivity
→ setContent
→ TeamTaskAppTheme
→ Surface
→ HomeScreen
```

### 2. 设计项目包结构

当前核心包结构如下：

```text
com.nep.teamtask
├── data
│   ├── model
│   └── fake
└── ui
    ├── theme
    ├── screen
    ├── component
    └── navigation
```

### 3. 配置 Material 3 主题

已完成：

* `Color.kt`
* `Type.kt`
* `Theme.kt`

主题系统支持：

* Light Theme
* Dark Theme
* Dynamic Color
* Typography
* Shapes
* Spacing Token

### 4. 创建基础数据模型

已创建任务模型：

```kotlin
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val assignee: String,
    val dueDate: String
)
```

任务状态：

```kotlin
enum class TaskStatus {
    Todo,
    InProgress,
    Done
}
```

### 5. 创建 Fake 数据仓库

使用 `FakeTaskRepository` 提供本地假数据，为后续任务列表页面做准备。

## 当前验收结果

本阶段完成后，项目应满足：

* App 可以正常运行
* `MainActivity` 职责清晰
* UI、数据、主题、导航目录已拆分
* 页面不直接管理主题细节
* 颜色、字体、圆角、间距统一从 theme 层读取
* 后续可以平滑进入页面导航和状态管理阶段

## 阶段总结

Phase 1 的核心成果是完成了 TeamTaskApp 的工程骨架。

这一阶段虽然页面内容较少，但已经建立了后续企业级 Compose 项目的基础约束：

* 页面放在 `ui/screen`
* 复用组件放在 `ui/component`
* 主题规范放在 `ui/theme`
* 路由信息放在 `ui/navigation`
* 数据模型放在 `data/model`
* 假数据放在 `data/fake`

后续开发应继续保持这个分层方式，避免把业务数据、UI 组件、页面逻辑和主题样式混在同一个文件中。
