# Phase 02：数据模型与 Fake 数据源

## 阶段目标

Phase 02 的目标是为 TeamTaskApp 准备业务数据基础。当前阶段暂时不接入真实后端，而是通过本地数据模型和 Fake Repository 模拟企业任务协作场景。

这样可以让 UI 开发不依赖后端进度，提前完成页面、状态、交互和异常场景的设计。

## 为什么先做数据模型

在企业项目中，UI 页面通常不是孤立存在的。页面展示的数据、表单提交的数据、错误状态和权限状态，都需要依赖清晰的数据结构。

因此在正式编写首页、详情页和创建页之前，需要先定义：

- 用户模型
- 任务模型
- 任务状态枚举
- 本地 Fake 数据源
- 请求结果模型

## 数据模型设计

### User

`User` 表示任务负责人。

```kotlin
data class User(
    val id: String,
    val name: String,
    val role: String,
    val avatarUrl: String? = null
)
````

字段说明：

|字段|说明|
|---|---|
|`id`|用户唯一 ID|
|`name`|用户名称|
|`role`|用户角色|
|`avatarUrl`|用户头像地址，当前阶段允许为空|

当前阶段头像不是核心功能，所以 `avatarUrl` 使用可空类型。后续 UI 可以在头像为空时显示姓名首字母占位图。

### TaskStatus

`TaskStatus` 表示任务状态。

```kotlin
enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    BLOCKED
}
```

状态说明：

|状态|说明|
|---|---|
|`TODO`|待处理|
|`IN_PROGRESS`|进行中|
|`DONE`|已完成|
|`BLOCKED`|被阻塞|

使用 `enum class` 的好处是状态值固定、类型安全，不容易出现字符串拼写错误。

### Task

`Task` 表示团队任务。

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

字段说明：

|字段|说明|
|---|---|
|`id`|任务唯一 ID|
|`title`|任务标题|
|`description`|任务描述|
|`status`|任务状态|
|`assignee`|负责人|
|`dueDate`|截止日期|

当前阶段 `dueDate` 暂时使用 `String`，方便 UI 快速展示。后续接入真实后端或数据库时，可以再升级为 `LocalDate`、`Instant` 或专门的 UI Model。

## Fake Repository 设计

当前新增：

```text
data/fake/FakeTaskRepository.kt
```

Fake Repository 的职责是模拟真实数据源，为 UI 开发提供稳定数据。

它目前支持：

- 获取任务列表

- 获取任务详情

- 创建任务

- 获取用户列表

- 切换模拟场景


## 模拟场景

### 1. 正常有数据

```kotlin
FakeTaskRepository.setScenario(FakeTaskScenario.NORMAL)
```

用于模拟接口正常返回任务列表。

适合开发：

- 首页任务列表

- 详情页

- 创建任务后的列表刷新


### 2. 空数据

```kotlin
FakeTaskRepository.setScenario(FakeTaskScenario.EMPTY)
```

用于模拟请求成功，但是没有任务数据。

这不是错误状态，而是空状态。UI 应该显示：

```text
暂无任务
创建一个新任务开始协作
```

### 3. 加载失败

```kotlin
FakeTaskRepository.setScenario(FakeTaskScenario.LOAD_FAILED)
```

用于模拟网络异常、服务器异常或未知错误。

UI 应该显示：

```text
加载失败
请稍后重试
```

并提供重试按钮。

### 4. 权限不足

```kotlin
FakeTaskRepository.setScenario(FakeTaskScenario.NO_PERMISSION)
```

用于模拟当前用户没有权限访问团队任务。

UI 应该显示：

```text
无权限访问
请联系团队管理员开通权限
```

权限不足应与普通网络错误分开处理，因为它在企业项目中属于独立业务状态。

## 请求结果设计

Fake Repository 使用 `FakeTaskResult` 表示请求结果。

```kotlin
sealed interface FakeTaskResult<out T> {
    data class Success<T>(val data: T) : FakeTaskResult<T>

    data class Failure(
        val message: String
    ) : FakeTaskResult<Nothing>

    data class NoPermission(
        val message: String = "当前账号没有权限查看团队任务"
    ) : FakeTaskResult<Nothing>
}
```

这样设计的好处是：UI 层可以明确区分成功、失败和无权限。

示例：

```kotlin
when (val result = FakeTaskRepository.getTasks()) {
    is FakeTaskResult.Success -> {
        val tasks = result.data

        if (tasks.isEmpty()) {
            // Empty UI
        } else {
            // Content UI
        }
    }

    is FakeTaskResult.Failure -> {
        // Error UI
    }

    is FakeTaskResult.NoPermission -> {
        // No Permission UI
    }
}
```

## 当前阶段完成内容

本阶段已完成：

- `User.kt`

- `TaskStatus.kt`

- `Task.kt`

- `FakeTaskRepository.kt`

- 正常数据模拟

- 空数据模拟

- 加载失败模拟

- 权限不足模拟

- 为 UI State 做准备


## 当前阶段学习重点

### data class

`data class` 适合表示不可变状态数据，例如任务、用户、页面状态。

它自动提供：

- `toString`

- `equals`

- `hashCode`

- `copy`


后续修改任务状态时，不建议直接修改原对象，而是通过 `copy` 创建新对象。

### enum class

`enum class` 适合表示固定范围的状态，例如任务状态。

相比字符串，枚举更安全。

错误示例：

```kotlin
val status = "DOING"
```

正确示例：

```kotlin
val status = TaskStatus.IN_PROGRESS
```

### Fake 数据源

Fake 数据源可以让前端或客户端开发不依赖真实后端。

它适合用于：

- 页面原型开发

- UI 状态验证

- 表单流程调试

- 异常场景模拟

- 后端接口未完成时提前推进开发