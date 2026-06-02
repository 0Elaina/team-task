# Phase 03 - 通用 UI 状态设计

## 1. 阶段目标

本阶段的目标是为 TeamTaskApp 设计一套通用的 UI 状态处理方案，使页面不再只考虑“成功状态”，而是完整覆盖真实企业项目中常见的加载中、无数据、加载失败、加载成功等状态。

在企业级 Android 项目中，页面不能只写正常数据展示逻辑。真实业务中经常会遇到网络慢、接口失败、数据为空、权限不足、服务器异常等情况。如果没有统一的状态建模和通用状态页面，项目会逐渐出现大量重复代码，并且不同页面的错误处理风格不一致。

因此，本阶段通过 `UiState` 和通用状态组件，为后续任务列表页、任务详情页、创建页、个人页提供统一的 UI 状态基础。

## 2. 已完成内容

### 2.1 定义通用 UiState

新增文件：

```text
ui/state/UiState.kt
```

核心代码：

```kotlin
sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data class Success<T>(
        val data: T
    ) : UiState<T>

    data object Empty : UiState<Nothing>

    data class Error(
        val message: String
    ) : UiState<Nothing>
}
```

`UiState` 统一描述页面状态：

| 状态           | 含义              |
| ------------ | --------------- |
| `Loading`    | 页面正在加载数据        |
| `Success<T>` | 页面成功获取数据        |
| `Empty`      | 页面请求成功，但没有可展示数据 |
| `Error`      | 页面加载失败，需要展示错误信息 |

### 2.2 设计通用 LoadingView

新增文件：

```text
ui/component/LoadingView.kt
```

该组件用于展示加载中状态，主要使用：

* `CircularProgressIndicator`
* `Text`
* `Column`
* `MaterialTheme`

适用场景：

* 页面首次进入时加载数据
* 网络请求等待中
* 刷新数据时需要明确反馈用户

### 2.3 设计通用 EmptyView

新增文件：

```text
ui/component/EmptyView.kt
```

该组件用于展示空数据状态，主要使用：

* `Icon`
* `Text`
* `Button`
* 空状态说明文案
* 可选引导操作按钮

适用场景：

* 当前团队暂无任务
* 当前用户暂无负责事项
* 搜索结果为空
* 没有可展示的数据列表

空状态不是错误状态。空状态表示请求成功，但数据结果为空，因此应该通过友好的说明和引导操作帮助用户继续使用产品。

### 2.4 设计通用 ErrorView

新增文件：

```text
ui/component/ErrorView.kt
```

该组件用于展示错误状态，主要使用：

* `Icon`
* `Text`
* `Button`
* 错误说明
* 重试按钮

适用场景：

* 网络请求失败
* 服务端异常
* 权限不足
* 数据解析失败
* 页面加载失败

企业项目中的错误页面不能只显示“出错了”，必须提供可恢复操作，例如“重试”。

## 3. 当前目录结构

当前阶段完成后，核心目录结构如下：

```text
TeamTaskApp/
└── app/
    └── src/
        └── main/
            └── java/
                └── com/nep/teamtask/
                    ├── data/
                    │   ├── fake/
                    │   └── model/
                    └── ui/
                        ├── component/
                        │   ├── LoadingView.kt
                        │   ├── EmptyView.kt
                        │   └── ErrorView.kt
                        ├── screen/
                        ├── state/
                        │   └── UiState.kt
                        └── theme/
```

## 4. 设计原则

### 4.1 页面必须覆盖多种状态

每个页面都应该至少考虑以下状态：

```text
Loading -> Empty -> Error -> Success
```

不能只写：

```text
Success UI
```

因为真实项目中的数据请求并不总是成功。

### 4.2 状态模型与业务模型解耦

`UiState<T>` 是 UI 层状态模型，不直接依赖具体业务对象。

例如：

```kotlin
UiState<List<Task>>
UiState<Task>
UiState<User>
UiState<Unit>
```

同一套状态模型可以服务于任务列表页、任务详情页、创建任务页和个人页。

### 4.3 通用组件不能绑定具体业务

`LoadingView`、`EmptyView`、`ErrorView` 不应该依赖 `Task`、`User` 等业务模型，只接收文本、点击事件和 `Modifier`。

这样可以提高组件复用性，避免每个页面重复写加载、空态和错误态 UI。

### 4.4 ErrorView 必须提供重试能力

错误状态应该提供恢复入口，例如：

```kotlin
onRetryClick: () -> Unit
```

用户看到错误后，应该能够主动触发重新加载，而不是只能退出页面或重启应用。

## 5. 示例使用方式

任务列表页可以使用如下结构：

```kotlin
@Composable
fun TaskListScreen(
    uiState: UiState<List<Task>>,
    onRetryClick: () -> Unit,
    onCreateTaskClick: () -> Unit
) {
    when (uiState) {
        UiState.Loading -> {
            LoadingView()
        }

        UiState.Empty -> {
            EmptyView(
                title = "暂无任务",
                description = "当前团队还没有任务，创建第一个任务开始协作",
                actionText = "创建任务",
                onActionClick = onCreateTaskClick
            )
        }

        is UiState.Error -> {
            ErrorView(
                title = "任务加载失败",
                message = uiState.message,
                onRetryClick = onRetryClick
            )
        }

        is UiState.Success -> {
            TaskListContent(
                tasks = uiState.data
            )
        }
    }
}
```

## 6. 本阶段学习重点

通过本阶段，需要重点理解：

1. `sealed interface` 适合描述有限状态集合。
2. 泛型 `UiState<T>` 可以统一封装不同页面的数据状态。
3. `Loading`、`Empty`、`Error` 不应该散落在每个页面中重复实现。
4. Compose 页面应该根据状态驱动 UI，而不是通过多个布尔变量拼接页面逻辑。
5. 企业级 UI 设计必须考虑失败、空数据、慢加载和用户恢复操作。

## 7. 当前阶段验收标准

本阶段完成后，应满足以下标准：

* 已创建 `UiState.kt`
* 已创建 `LoadingView.kt`
* 已创建 `EmptyView.kt`
* 已创建 `ErrorView.kt`
* 页面状态结构能够覆盖 Loading、Empty、Error、Success
* 错误状态具有重试按钮
* 空状态支持引导操作
* 通用状态组件不依赖具体业务模型
* README 已更新当前阶段进度
