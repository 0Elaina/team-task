# Phase 04：页面导航 Navigation

## 1. 阶段目标

本阶段目标是为 TeamTaskApp 搭建基础页面导航结构，让项目从“单页面 Demo”升级为“多页面 App 原型”。

当前项目需要支持四类页面：

| 页面  | 作用                    |
| --- | --------------------- |
| 首页  | 展示任务列表                |
| 详情页 | 展示任务标题、描述、状态、负责人、截止日期 |
| 创建页 | 通过表单创建任务              |
| 个人页 | 展示用户信息和设置入口           |

本阶段重点不是把每个页面做完整，而是先建立清晰、可维护的导航结构。

需要掌握：

* `NavHost`
* `composable`
* 路由常量管理
* 带参数页面跳转
* `navController.navigate()`
* `popBackStack()`
* `Scaffold`
* `NavigationBar`
* `NavigationBarItem`
* 当前底部 Tab 选中状态

## 2. 为什么要先设计 Navigation

在真实企业项目中，页面跳转不能随意写在各个页面里。

如果每个页面都直接拼接字符串路由，例如：

```kotlin
navController.navigate("detail/$taskId")
```

项目变大后会出现几个问题：

1. 路由字符串散落在各处，后期维护困难。
2. 页面参数名称不统一，容易出现拼写错误。
3. 一级页面和二级页面职责混乱。
4. 底部导航、返回栈、页面跳转规则难以统一管理。
5. 页面组件直接依赖 `NavController`，不利于复用和测试。

因此本阶段将路由集中放到 `ui/navigation` 包下管理。

## 3. 路由设计

新增或完善文件：

```text
ui/navigation/TeamTaskRoutes.kt
```

推荐结构：

```kotlin
package com.nep.teamtask.ui.navigation

import android.net.Uri

object TeamTaskRoutes {
    const val HOME = "home"
    const val CREATE_TASK = "create_task"
    const val PROFILE = "profile"

    const val ARG_TASK_ID = "taskId"
    const val TASK_DETAIL = "task_detail/{$ARG_TASK_ID}"

    fun taskDetail(taskId: String): String {
        return "task_detail/${Uri.encode(taskId)}"
    }
}
```

这里需要区分两个概念：

| 内容                                     | 作用           |
| -------------------------------------- | ------------ |
| `TASK_DETAIL = "task_detail/{taskId}"` | 注册详情页路由模板    |
| `taskDetail(taskId)`                   | 跳转详情页时生成真实路由 |

这样做的好处是：页面跳转时不需要到处手写字符串，后续如果路由规则变化，只需要修改 `TeamTaskRoutes`。

## 4. 页面导航图设计

新增或完善文件：

```text
ui/navigation/TeamTaskNavHost.kt
```

`TeamTaskNavHost` 是整个 App 的导航入口，负责：

* 创建 `NavHostController`
* 监听当前路由
* 承载 `Scaffold`
* 根据当前页面决定是否显示底部导航栏
* 注册首页、详情页、创建页、个人页
* 处理页面跳转和返回

导航结构如下：

```text
TeamTaskNavHost
├── Scaffold
│   └── bottomBar: TeamTaskBottomBar
└── NavHost
    ├── home
    ├── task_detail/{taskId}
    ├── create_task
    └── profile
```

## 5. 页面跳转关系

当前页面跳转关系为：

```text
HomeScreen
├── 点击任务卡片 -> TaskDetailScreen(taskId)
├── 点击创建任务 -> CreateTaskScreen
└── 点击底部 Profile -> ProfileScreen

ProfileScreen
└── 点击底部 Tasks -> HomeScreen

TaskDetailScreen
└── 返回 -> 上一页

CreateTaskScreen
├── 创建成功 -> 上一页
└── 返回 -> 上一页
```

这里需要注意：`Home` 和 `Profile` 是一级页面，`TaskDetail` 和 `CreateTask` 是二级页面。

## 6. 底部导航栏设计

本阶段新增底部导航栏，只包含两个 Tab：

| Tab     | 页面  | 路由        |
| ------- | --- | --------- |
| Tasks   | 首页  | `home`    |
| Profile | 个人页 | `profile` |

底部导航栏不应该包含详情页和创建页。

原因是：

1. 详情页必须从某个任务进入，依赖 `taskId` 参数。
2. 创建页属于临时操作流程，不是常驻一级页面。
3. 底部导航栏应该只放 App 的主要功能入口。
4. 二级页面显示底部栏会让返回层级变得混乱。

## 7. 底部导航 Item 设计

新增文件：

```text
ui/navigation/TeamTaskBottomNavItem.kt
```

推荐结构：

```kotlin
package com.nep.teamtask.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class TeamTaskBottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val teamTaskBottomNavItems = listOf(
    TeamTaskBottomNavItem(
        route = TeamTaskRoutes.HOME,
        label = "Tasks",
        icon = Icons.Filled.Home
    ),
    TeamTaskBottomNavItem(
        route = TeamTaskRoutes.PROFILE,
        label = "Profile",
        icon = Icons.Filled.Person
    )
)
```

该文件只描述底部导航栏的数据，不负责具体 UI 渲染。

## 8. 底部导航栏组件设计

新增文件：

```text
ui/navigation/TeamTaskBottomBar.kt
```

`TeamTaskBottomBar` 负责渲染底部导航栏，主要使用：

* `NavigationBar`
* `NavigationBarItem`
* `Icon`
* `Text`

核心逻辑：

```kotlin
val selected = currentDestination
    ?.hierarchy
    ?.any { destination ->
        destination.route == item.route
    } == true
```

这段代码用于判断当前页面是否属于某个底部 Tab，从而显示选中状态。

点击底部 Tab 时使用：

```kotlin
navController.navigate(item.route) {
    popUpTo(TeamTaskRoutes.HOME) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
```

参数说明：

| 参数                | 作用                      |
| ----------------- | ----------------------- |
| `popUpTo`         | 控制返回栈，避免 Tab 切换导致页面堆叠混乱 |
| `saveState`       | 保存底部页面状态                |
| `launchSingleTop` | 避免重复点击同一个 Tab 时重复创建页面   |
| `restoreState`    | 回到 Tab 时恢复之前的页面状态       |

## 9. 底部栏显示规则

底部栏只在一级页面显示：

| 页面  | 路由                     | 是否显示底部栏 |
| --- | ---------------------- | ------- |
| 首页  | `home`                 | 是       |
| 个人页 | `profile`              | 是       |
| 详情页 | `task_detail/{taskId}` | 否       |
| 创建页 | `create_task`          | 否       |

推荐封装判断函数：

```kotlin
private fun NavDestination?.shouldShowBottomBar(): Boolean {
    val route = this?.route

    return route == TeamTaskRoutes.HOME ||
            route == TeamTaskRoutes.PROFILE
}
```

这样可以让底部栏显示规则集中管理，而不是分散在各个页面中。

## 10. MainActivity 调整

`MainActivity` 不应该直接显示 `HomeScreen`，而应该显示整个 App 的导航入口。

推荐结构：

```kotlin
setContent {
    TeamTaskAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TeamTaskNavHost()
        }
    }
}
```

这样 `MainActivity` 只负责启动 Compose UI，页面切换交给 navigation 层处理。

## 11. 本阶段完成内容

本阶段已完成：

* 引入 Compose Navigation
* 定义 `TeamTaskRoutes`
* 支持首页、详情页、创建页、个人页路由
* 支持详情页 `taskId` 参数
* 编写 `TeamTaskNavHost`
* 使用 `NavHost` 注册页面
* 使用 `composable` 定义页面节点
* 使用 `navController.navigate()` 实现页面跳转
* 使用 `popBackStack()` 实现返回
* 使用 `Scaffold` 承载页面结构
* 使用 `NavigationBar` 和 `NavigationBarItem` 实现底部导航栏
* 支持 `Tasks` 和 `Profile` 两个底部 Tab
* 支持当前 Tab 选中状态
* 详情页和创建页隐藏底部导航栏

## 12. 当前阶段学习重点

通过本阶段，需要重点理解：

1. `NavHost` 是 Compose Navigation 的导航容器。
2. `composable` 用于注册一个可以跳转的页面。
3. 路由字符串应该集中管理，不能散落在页面代码中。
4. 带参数页面需要同时定义路由模板和真实跳转路由。
5. 页面组件最好通过回调暴露跳转意图，而不是直接依赖 `NavController`。
6. `Scaffold` 可以统一承载顶部栏、底部栏、悬浮按钮和内容区。
7. `NavigationBar` 适合承载 App 的一级页面入口。
8. 详情页、创建页这类二级页面不适合放进底部导航栏。
9. 当前选中状态应由当前返回栈路由计算，而不是手动保存布尔值。
10. 返回栈管理是多页面 App 必须考虑的问题。

## 13. 当前阶段验收标准

本阶段完成后，应满足以下标准：

* App 启动后默认进入首页。
* 首页可以跳转到任务详情页。
* 任务详情页可以正确接收 `taskId`。
* 首页可以跳转到创建任务页。
* 详情页可以返回上一页。
* 创建页可以返回上一页。
* 底部导航栏包含 `Tasks` 和 `Profile`。
* 点击 `Profile` 可以进入个人页。
* 点击 `Tasks` 可以回到首页。
* 当前页面对应的底部 Tab 有选中状态。
* 详情页不显示底部导航栏。
* 创建页不显示底部导航栏。
* 连续点击同一个底部 Tab 不会重复堆叠页面。
* `MainActivity` 不再直接依赖具体页面，而是接入 `TeamTaskNavHost`。

## 14. 阶段总结

Phase 04 的核心成果是让 TeamTaskApp 从单页面结构升级为多页面导航结构。

本阶段完成后，项目已经具备了 App 原型的基本页面骨架：

```text
首页
├── 任务详情页
├── 创建任务页
└── 个人页
```

同时，项目初步形成了企业级 Android 项目的导航分层思想：

* 路由常量集中管理
* 页面跳转集中在 navigation 层
* 页面通过回调表达跳转意图
* 一级页面使用底部导航栏
* 二级页面通过普通页面栈进入
* 返回栈规则清晰可控

后续开发可以在此基础上继续完善首页任务列表、任务详情页、创建任务表单、个人页和各种 Loading / Empty / Error 状态。
