# TeamTaskApp

TeamTaskApp 是一个基于 Jetpack Compose 的团队任务协作原型项目。

当前项目处于学习阶段，目标是从一个基础 Compose Demo 逐步演进为具备页面导航、任务列表、详情页、创建页、个人页以及完整 UI 状态处理的 Android App 原型。

## 项目目标

通过 TeamTaskApp 学习并实践 Android 企业项目中常见的 Compose 架构与 UI 组织方式，包括：

* Compose 项目结构设计
* Material 3 主题系统
* 统一颜色、字体、间距、圆角规范
* 页面与组件拆分
* Navigation 页面导航
* 表单校验
* Loading / Empty / Error 状态处理
* Snackbar、Dialog 等交互组件
* Fake Repository 模拟数据层

## 当前阶段

### Phase 1：项目基础搭建

已完成内容：

* 创建基础 Jetpack Compose 项目
* 整理项目包结构
* 配置 `MainActivity`
* 使用 `setContent` 作为 Compose 入口
* 配置 `MaterialTheme`
* 使用 `Surface` 作为页面根容器
* 创建基础数据模型 `Task`
* 创建 Fake 数据仓库
* 配置 Material 3 主题系统
* 支持 Light / Dark Theme
* 支持 Android 12+ Dynamic Color
* 抽取统一 spacing token

## 当前目录结构

```text
app/src/main/java/com/nep/teamtask
├── MainActivity.kt
├── data
│   ├── model
│   │   └── Task.kt
│   └── fake
│       └── FakeTaskRepository.kt
└── ui
    ├── theme
    │   ├── Color.kt
    │   ├── Type.kt
    │   └── Theme.kt
    ├── screen
    │   └── HomeScreen.kt
    ├── component
    │   └── AppPlaceholder.kt
    └── navigation
        └── TeamTaskRoutes.kt
```

## 技术栈

* Kotlin
* Jetpack Compose
* Material 3
* Android Studio
* Gradle

## 主题系统

当前主题系统集中在：

```text
ui/theme
├── Color.kt
├── Type.kt
└── Theme.kt
```

主题层负责统一管理：

* `colorScheme`
* `typography`
* `shapes`
* `spacing`
* `darkTheme`
* `dynamicColor`

页面中应优先使用：

```kotlin
MaterialTheme.colorScheme.primary
MaterialTheme.typography.titleLarge
MaterialTheme.shapes.medium
TeamTaskTheme.spacing.large
```

避免在页面中直接硬编码：

```kotlin
Color(0xFF2563EB)
16.dp
RoundedCornerShape(12.dp)
```

## 下一阶段计划

### Phase 2：Compose 页面与导航

计划完成：

* 首页：任务列表
* 详情页：任务标题、描述、状态、负责人、截止日期
* 创建页：任务表单
* 个人页：用户信息与设置入口
* Navigation Compose 页面跳转
* Loading 状态
* Empty 状态
* Error 状态
* Snackbar
* Dialog
* 基础表单校验

## Git 提交规范

推荐使用 Conventional Commits：

```text
chore: project setup
feat: add task list screen
fix: handle empty task state
docs: update README
refactor: extract task card component
```

第一阶段推荐提交信息：

```text
chore: complete phase 1 project setup
```
