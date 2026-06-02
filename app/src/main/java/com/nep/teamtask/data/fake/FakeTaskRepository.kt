package com.nep.teamtask.data.fake

import android.R.attr.delay
import com.nep.teamtask.data.model.Task
import com.nep.teamtask.data.model.TaskStatus
import com.nep.teamtask.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay
import java.util.UUID

/**
 * 定义 FakeTaskRepository 的模拟场景，用于在开发和测试阶段模拟不同的接口返回状态。
 *
 * - [NORMAL]: 正常返回任务列表
 * - [EMPTY]: 返回空任务列表
 * - [LOAD_FAILED]: 模拟加载失败，返回网络错误
 * - [NO_PERMISSION]: 模拟当前账号无权限查看团队任务
 */
enum class FakeTaskScenario {
    NORMAL,
    EMPTY,
    LOAD_FAILED,
    NO_PERMISSION
}

/**
 * FakeTaskRepository 操作的结果类型，封装成功、失败、无权限三种状态。
 *
 * - [Success]: 操作成功，携带返回数据
 * - [Failure]: 操作失败，携带错误信息（如网络错误）
 * - [NoPermission]: 当前账号无权限，携带提示信息
 */
sealed interface FakeTaskResult<out T> {
    data class Success<T>(val data: T) : FakeTaskResult<T>

    data class Failure(
        val message: String
    ) : FakeTaskResult<Nothing>

    data class NoPermission(
        val message: String = "当前账号没有权限查看团队任务"
    ) : FakeTaskResult<Nothing>
}

/**
 * 模拟的任务仓库，提供伪造的本地数据源，用于 UI 开发、自动化测试和演示场景。
 *
 * 设计要点：
 * - 所有异步方法都带有 [FAKE_DELAY_MILLIS] 的模拟延迟，以贴近真实网络请求体验
 * - 通过 [setScenario] 切换 [FakeTaskScenario]，可在不同状态之间快速切换，方便测试空数据、加载失败等边界情况
 * - 数据存储在内存中（[taskStorage]），进程重启后重置，符合 fake 数据源的预期行为
 */
object FakeTaskRepository {
    /** 模拟网络请求的固定延迟时间（毫秒） */
    private const val FAKE_DELAY_MILLIS = 800L

    /** 当前仓库所处的模拟场景，决定各接口的返回行为 */
    private var scenario: FakeTaskScenario = FakeTaskScenario.NORMAL

    /**
     * 模拟的团队成员列表，包含产品经理、Android 开发、后端开发、UI 设计师四种角色，
     * 用于 [Task.assignee] 字段的预填充。
     */
    private val users = listOf(
        User(
            id = "u_001",
            name = "张明",
            role = "产品经理",
            avatarUrl = null
        ),
        User(
            id = "u_002",
            name = "李娜",
            role = "Android 开发",
            avatarUrl = null
        ),
        User(
            id = "u_003",
            name = "王强",
            role = "后端开发",
            avatarUrl = null
        ),
        User(
            id = "u_004",
            name = "陈雨",
            role = "UI 设计师",
            avatarUrl = null
        )
    )

    /**
     * 模拟的任务存储列表，以可变列表形式维护，支持新增操作。
     * 预填充 5 条示例任务，覆盖 TODO、IN_PROGRESS、BLOCKED、DONE 四种状态。
     */
    private val taskStorage = mutableListOf(
        Task(
            id = "task_001",
            title = "完成首页任务列表 UI",
            description = "使用 Jetpack Compose 实现任务列表, 包括任务标题、负责人、状态和截止日期",
            status = TaskStatus.IN_PROGRESS,
            assignee = users[1],
            dueDate = "2026-06-03"
        ),
        Task(
            id = "task_002",
            title = "设计任务详情页",
            description = "详情页需要展示任务标题、描述、状态、负责人和截止日期, 并预留后续编辑入口",
            status = TaskStatus.TODO,
            assignee = users[3],
            dueDate = "2026-06-05"
        ),
        Task(
            id = "task_003",
            title = "定义任务创建表单校验规则",
            description = "任务标题不能为空，描述不能少于 10 个字符，截止日期不能为空。",
            status = TaskStatus.TODO,
            assignee = users[0],
            dueDate = "2026-06-07"
        ),
        Task(
            id = "task_004",
            title = "修复任务状态同步问题",
            description = "模拟企业项目中的阻塞任务：任务状态更新后，列表页和详情页显示不一致。",
            status = TaskStatus.BLOCKED,
            assignee = users[2],
            dueDate = "2026-06-02"
        ),
        Task(
            id = "task_005",
            title = "整理 Material 3 主题规范",
            description = "统一 TeamTask 的颜色、字体、圆角、间距和组件使用规范。",
            status = TaskStatus.DONE,
            assignee = users[3],
            dueDate = "2026-05-30"
        )
    )

    /**
     * 切换当前仓库的模拟场景，影响后续所有异步接口的返回值。
     *
     * 典型用法：
     * - 在 UI 预览或测试中调用 [setScenario] 传入不同场景，验证不同状态下的界面表现
     * - 切换场景后，下一次调用 [getTasks] 或 [getTaskById] 等接口时生效
     */
    fun setScenario(newScenario: FakeTaskScenario) {
        scenario = newScenario
    }

    /**
     * 同步返回预设的团队成员列表，不受当前 [scenario] 影响。
     *
     * 返回值为只读列表，调用方无需担心数据被意外修改。
     */
    fun getUsers(): List<User> {
        return users
    }

    /**
     * 获取任务列表的挂起函数。
     *
     * 返回值根据当前 [scenario] 决定：
     * - [FakeTaskScenario.NORMAL]: 返回任务列表的副本（避免调用方修改内部数据）
     * - [FakeTaskScenario.EMPTY]: 返回空列表
     * - [FakeTaskScenario.LOAD_FAILED]: 返回 [FakeTaskResult.Failure] 并附带网络错误提示
     * - [FakeTaskScenario.NO_PERMISSION]: 返回 [FakeTaskResult.NoPermission] 并附带权限提示
     */
    suspend fun getTasks(): FakeTaskResult<List<Task>> {
        delay(FAKE_DELAY_MILLIS)

        return when (scenario) {
            FakeTaskScenario.NORMAL -> {
                FakeTaskResult.Success(taskStorage.toList())
            }
            FakeTaskScenario.EMPTY -> {
                FakeTaskResult.Success(emptyList())
            }
            FakeTaskScenario.LOAD_FAILED -> {
                FakeTaskResult.Failure("任务加载失败, 请检查网络后重试")
            }
            FakeTaskScenario.NO_PERMISSION -> {
                FakeTaskResult.NoPermission()
            }
        }
    }

    /**
     * 根据任务 ID 获取单个任务的挂起函数。
     *
     * 返回值根据当前 [scenario] 决定：
     * - [FakeTaskScenario.NORMAL]: 在 [taskStorage] 中查找匹配 ID 的任务，找到返回 [FakeTaskResult.Success]，未找到返回 [FakeTaskResult.Failure]("任务不存在")
     * - [FakeTaskScenario.EMPTY]: 直接返回 [FakeTaskResult.Failure]("任务不存在")
     * - [FakeTaskScenario.LOAD_FAILED]: 返回 [FakeTaskResult.Failure] 并附带网络错误提示
     * - [FakeTaskScenario.NO_PERMISSION]: 返回 [FakeTaskResult.NoPermission] 并附带权限提示
     */
    suspend fun getTaskById(taskId: String): FakeTaskResult<Task> {
        delay(FAKE_DELAY_MILLIS)

        return when (scenario) {
            FakeTaskScenario.LOAD_FAILED -> {
                FakeTaskResult.Failure("任务加载失败, 请稍后重试")
            }
            FakeTaskScenario.NO_PERMISSION -> {
                FakeTaskResult.NoPermission("当前账号没有权限查看该任务详情")
            }
            FakeTaskScenario.EMPTY -> {
                FakeTaskResult.Failure("任务不存在")
            }
            FakeTaskScenario.NORMAL -> {
                val task = taskStorage.firstOrNull {it.id == taskId}

                if(task != null) {
                    FakeTaskResult.Success(task)
                }else{
                    FakeTaskResult.Failure("任务不存在")
                }
            }
        }
    }

    /**
     * 创建新任务并插入到列表头部的挂起函数。
     *
     * 新任务的 ID 由 [UUID.randomUUID] 自动生成，创建后插入 [taskStorage] 头部（索引 0），
     * 以便在列表中优先展示。
     *
     * 返回值根据当前 [scenario] 决定：
     * - [FakeTaskScenario.NORMAL] 或 [FakeTaskScenario.EMPTY]: 新建任务并返回 [FakeTaskResult.Success]
     * - [FakeTaskScenario.LOAD_FAILED]: 返回 [FakeTaskResult.Failure] 并附带网络错误提示
     * - [FakeTaskScenario.NO_PERMISSION]: 返回 [FakeTaskResult.NoPermission] 并附带权限提示
     *
     * @param title 任务标题，不可为空
     * @param description 任务描述，不可少于 10 个字符
     * @param status 任务初始状态
     * @param assignee 任务负责人
     * @param dueDate 截止日期，格式为 "yyyy-MM-dd"
     */
    suspend fun createTask(
        title: String,
        description: String,
        status: TaskStatus,
        assignee: User,
        dueDate: String
    ): FakeTaskResult<Task> {
        delay(FAKE_DELAY_MILLIS)

        return when (scenario) {
            FakeTaskScenario.LOAD_FAILED -> {
                FakeTaskResult.Failure("任务创建失败, 请稍后重试")
            }
            FakeTaskScenario.NO_PERMISSION -> {
                FakeTaskResult.NoPermission("当前账号没有权限创建任务")
            }

            FakeTaskScenario.EMPTY, FakeTaskScenario.NORMAL -> {
                val newTask = Task(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    status = status,
                    assignee = assignee,
                    dueDate = dueDate
                )
                taskStorage.add(0, newTask)
                FakeTaskResult.Success(newTask)
            }
        }
    }
}