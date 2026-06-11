package com.nep.teamtask.ui.screen

// ==================== Jetpack Compose 基础 ====================
import androidx.compose.foundation.clickable                          // 为组件添加点击交互
import androidx.compose.foundation.layout.Arrangement                 // 布局排列方式
import androidx.compose.foundation.layout.Box                         // 简单容器，用于层的叠加
import androidx.compose.foundation.layout.Column                      // 垂直方向线性布局
import androidx.compose.foundation.layout.PaddingValues                // 内边距值对象
import androidx.compose.foundation.layout.Row                         // 水平方向线性布局
import androidx.compose.foundation.layout.Spacer                      // 空白占位组件
import androidx.compose.foundation.layout.fillMaxSize                 // 填充父容器最大尺寸
import androidx.compose.foundation.layout.fillMaxWidth                // 填充父容器最大宽度
import androidx.compose.foundation.layout.height                      // 设置高度
import androidx.compose.foundation.layout.padding                     // 设置内边距
import androidx.compose.foundation.layout.widthIn                     // 设置最小/最大宽度约束
import androidx.compose.foundation.lazy.LazyColumn                    // 懒加载列，只渲染可见项
import androidx.compose.foundation.lazy.items                        // 为 LazyColumn 绑定列表数据
// ==================== Material Icons ====================
import androidx.compose.material.icons.Icons                         // Material Icons 入口
import androidx.compose.material.icons.filled.Add                    // "+" 添加图标
// ==================== Material3 组件 ====================
import androidx.compose.material3.Card                               // 卡片容器组件
import androidx.compose.material3.CardDefaults                       // 卡片默认配置
import androidx.compose.material3.ExperimentalMaterial3Api           // Material3 实验性 API 标记
import androidx.compose.material3.FloatingActionButton               // 浮动操作按钮 (FAB)
import androidx.compose.material3.Icon                               // 图标组件
import androidx.compose.material3.MaterialTheme                      // Material 主题，提供颜色/字体/形状
import androidx.compose.material3.Scaffold                           // Material3 页面骨架
import androidx.compose.material3.Surface                            // 带背景色和形状的表面容器
import androidx.compose.material3.Text                               // 文本组件
import androidx.compose.material3.TopAppBar                          // 顶部应用栏
// ==================== Compose 运行时 ====================
import androidx.compose.runtime.Composable                           // 可组合函数标记
import androidx.compose.runtime.LaunchedEffect                       // 在协程作用域内执行副作用
import androidx.compose.runtime.getValue                             // 委托属性的 getter
import androidx.compose.runtime.mutableIntStateOf                    // 可观察的 int 状态
import androidx.compose.runtime.mutableStateOf                       // 可观察的泛型状态
import androidx.compose.runtime.remember                             // 记住值，在重组中保持
import androidx.compose.runtime.setValue                             // 委托属性的 setter
// ==================== Compose UI 基础 ====================
import androidx.compose.ui.Alignment                                 // 子组件对齐方式
import androidx.compose.ui.Modifier                                  // 修饰符，用于布局/样式/交互
import androidx.compose.ui.text.font.FontWeight                      // 字体粗细
import androidx.compose.ui.text.style.TextOverflow                   // 文本溢出处理方式
import androidx.compose.ui.unit.dp                                   // dp 密度无关像素单位
// ==================== 项目内部依赖 ====================
import com.nep.teamtask.data.fake.FakeTaskRepository                 // 伪造任务数据仓库（开发/演示用）
import com.nep.teamtask.data.fake.FakeTaskResult                     // 仓库操作结果封装
import com.nep.teamtask.data.model.Task                              // 任务数据模型
import com.nep.teamtask.data.model.TaskStatus                        // 任务状态枚举
import com.nep.teamtask.data.model.toDisplayText                     // 任务状态 → 显示文本扩展
import com.nep.teamtask.ui.component.EmptyView                       // 空数据占位视图
import com.nep.teamtask.ui.component.ErrorView                       // 错误提示视图
import com.nep.teamtask.ui.component.LoadingView                     // 加载中提示视图
import com.nep.teamtask.ui.state.UiState                             // UI 状态密封类（Loading/Empty/Error/Success）
import com.nep.teamtask.ui.theme.TeamTaskTheme                       // 项目自定义主题（间距、形状等）

/**
 * HomeScreen —— 应用首页主组合函数。
 *
 * 作为应用的主入口页面，承担以下职责：
 * 1. **数据加载**：进入页面时自动触发任务列表加载（通过 LaunchedEffect）
 * 2. **状态分发**：根据数据加载结果（Loading / Empty / Error / Success）渲染不同的 UI
 * 3. **用户交互**：提供创建任务的 FAB、任务卡片的点击、错误重试等交互入口
 * 4. **导航回调**：通过参数将导航行为上抛给调用方（NavHost）
 *
 * ## 状态设计
 * - `taskUiState`：用密封类 `UiState<T>` 表达四种互斥状态，保证同一时刻只有一种状态生效
 * - `reloadKey`：作为重试触发器，自增后重新执行 LaunchedEffect 拉取数据
 *
 * @param onTaskClick 任务卡片点击回调，将任务 ID 传递给导航层以跳转详情页
 * @param onCreateTaskClick FAB 或 EmptyView 中"创建任务"按钮的回调，跳转创建页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTaskClick: (String) -> Unit,
    onCreateTaskClick: () -> Unit,
) {
    // ==================== 状态定义 ====================

    /**
     * 任务列表的 UI 状态。
     * 泛型参数 `UiState<List<Task>>` 表示成功时携带一个任务列表作为数据载荷。
     * 初始值为 `UiState.Loading`，保证进入页面时立即显示加载视图。
     */
    var taskUiState by remember { mutableStateOf<UiState<List<Task>>>(UiState.Loading) }

    /**
     * 重新加载键（计数器）。
     * 当用户点击"重新加载"时递增该值，LaunchedEffect 依赖于此键，
     * 键值变化会取消旧协程并重新执行数据加载逻辑。
     * 使用 mutableIntStateOf 而非 mutableStateOf 以避免 int 的装箱开销。
     */
    var reloadKey by remember { mutableIntStateOf(0) }

    // ==================== 数据加载副作用 ====================

    /**
     * LaunchedEffect：在 Composable 进入组合时启动协程，在键变化时重启协程。
     * 这里依赖 reloadKey，因此:
     * - 首次进入时立即加载数据
     * - 用户点击重试（reloadKey++）时重新加载
     *
     * 注意：LaunchedEffect 会在离开组合或键变化时自动取消未完成的协程，
     * 避免在页面已离开后仍更新状态导致内存泄漏。
     */
    LaunchedEffect(reloadKey) {
        // 每次加载前将状态重置为 Loading，触发加载视图的显示
        taskUiState = UiState.Loading

        /**
         * 调用 FakeTaskRepository 获取任务数据。
         * 当前使用假数据仓库，后续可替换为真实的网络/数据库实现。
         * FakeTaskRepository.getTasks() 返回 FakeTaskResult 密封类:
         * - Success：数据获取成功，携带结果列表
         * - Failure：操作失败，携带错误信息
         * - NoPermission：无权限访问，携带提示信息
         */
        taskUiState = when (val result = FakeTaskRepository.getTasks()) {
            /**
             * 成功分支：进一步区分数据是否为空。
             * - 空列表 → UiState.Empty（显示空状态占位视图）
             * - 非空列表 → UiState.Success（显示任务列表内容）
             */
            is FakeTaskResult.Success -> {
                if (result.data.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(result.data)
                }
            }

            /**
             * 失败分支：统一映射为 UiState.Error。
             * result.message 携带具体的错误描述，传递给 ErrorView 展示。
             */
            is FakeTaskResult.Failure -> {
                UiState.Error(result.message)
            }

            /**
             * 无权限分支：目前与 Failure 同样处理为 Error 状态。
             * （可扩展：后续可以拆分为独立视图，提供重新登录等操作入口）
             */
            is FakeTaskResult.NoPermission -> {
                UiState.Error(result.message)
            }
        }
    }

    // ==================== 页面骨架布局 ====================

    /**
     * Scaffold —— Material3 页面骨架，提供标准化的页面结构:
     * - topBar：顶部应用栏
     * - floatingActionButton：浮动操作按钮（FAB）
     * - content：页面主体内容区域（通过 innerPadding 自动处理各组件占用的内边距）
     */
    Scaffold(
        // ----- 顶部应用栏 -----
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Team Tasks")   // 应用标题，居中显示于顶部
                }
            )
        },
        // ----- 浮动操作按钮 (FAB) -----
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateTaskClick    // 点击后导航到任务创建页面
            ) {
                Icon(
                    imageVector = Icons.Default.Add,     // "+" 图标
                    contentDescription = "创建任务"       // 无障碍描述
                )
            }
        }
    ) { innerPadding ->
        // ==================== 页面主体内容 ====================

        /**
         * Box 容器：填充剩余空间并根据 innerPadding 留出边缘间距。
         * 使用 Box 方便未来在列表之上叠加其他浮动元素（如 Snackbar）。
         */
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            // ==================== 状态驱动的视图切换 ====================

            /**
             * when 表达式完整覆盖 UiState 的四种分支。
             * 使用 `val state = taskUiState` 语法将状态捕获为局部变量，
             * 方便在 Error/Success 分支中访问其携带的数据。
             * Kotlin 编译器保证所有分支被穷举，新增 UiState 子类时会报编译错误。
             */
            when (val state = taskUiState) {
                // ----- 加载状态 -----
                UiState.Loading -> {
                    LoadingView(message = "正在加载任务列表...")
                }

                // ----- 空数据状态 -----
                UiState.Empty -> {
                    EmptyView(
                        title = "暂无任务",
                        description = "当前团队还没有任务, 带年纪右下角按钮创建第一个任务",
                        actionText = "创建任务",          // 按钮文本
                        onActionClick = onCreateTaskClick  // 点击后跳转创建页
                    )
                }

                // ----- 错误状态 -----
                is UiState.Error -> {
                    ErrorView(
                        message = state.message,   // 从 UiState.Error 中取出错误信息
                        retryText = "重新加载",
                        onRetryClick = {
                            reloadKey++            // 递增 reloadKey 触发 LaunchedEffect 重新加载
                        }
                    )
                }

                // ----- 成功状态（携带数据） -----
                is UiState.Success -> {
                    TaskListContent(
                        tasks = state.data,        // 从 UiState.Success 中取出任务列表
                        onTaskClick = onTaskClick
                    )
                }
            }
        }
    }
}

/**
 * TaskListContent —— 任务列表内容区域。
 *
 * 使用 LazyColumn 高效渲染任务列表，仅对可见区域内的项进行组合。
 * 包含两部分：
 * 1. **头部摘要**：固定显示在列表最上方（item {}）
 * 2. **任务卡片**：为每个任务渲染一张可点击的卡片（items {}）
 *
 * @param tasks 要展示的任务列表
 * @param onTaskClick 点击任务卡片时的回调，传递被点击任务的 ID
 */
@Composable
private fun TaskListContent(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit
) {
    /**
     * LazyColumn —— 垂直滚动的懒加载列表。
     * 与 Column 不同，LazyColumn 只组合和布局当前可见的项目，
     * 对长列表有更好的性能表现（回收不可见项的实例）。
     *
     * 布局参数说明：
     * - fillMaxSize：撑满父容器可用空间
     * - contentPadding：列表边缘留出大号间距（由 Theme 定义），使内容不紧贴屏幕边缘
     * - verticalArrangement.spacedBy：列表项之间的垂直间距（中号）
     */
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(TeamTaskTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(TeamTaskTheme.spacing.medium)
    ) {
        // ----- 列表头部：任务摘要统计 -----
        // 使用 item {} 添加一个非重复类型的列表项（始终位于列表顶部）
        item {
            HomeSummaryHeader(taskCount = tasks.size)
        }

        // ----- 列表主体：任务卡片循环 -----
        /**
         * items() 是 LazyColumn 的扩展函数，用于绑定 Iterable 数据。
         * key 参数为每个项指定稳定且唯一的标识符（任务 ID），
         * 帮助 Compose 准确追踪项的变化（新增/删除/移动/修改），
         * 避免不必要的重组，并维持滚动位置。
         */
        items(
            items = tasks,
            key = { task -> task.id }
        ) { task ->
            TaskCard(
                task = task,
                onClick = { onTaskClick(task.id) }
            )
        }
    }
}

/**
 * HomeSummaryHeader —— 首页任务列表的摘要头部。
 *
 * 位于任务列表顶部，以垂直 Column 展示：
 * - **主标题**："任务列表"（headlineSmall 样式，半粗体）
 * - **副标题**：当前任务总数统计文本（bodyMedium 样式，次强调色）
 *
 * @param taskCount 当前任务总数，用于在副标题中显示统计信息
 */
@Composable
private fun HomeSummaryHeader(
    taskCount: Int
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = TeamTaskTheme.spacing.small)
    ) {
        // ----- 主标题：固定文本 "任务列表" -----
        Text(
            text = "任务列表",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )

        // 标题与统计文本之间的间隔（极小号）
        Spacer(modifier = Modifier.height(TeamTaskTheme.spacing.extraSmall))

        // ----- 副标题：动态显示任务总数 -----
        // 使用字符串模板将数字插入到显示文本中
        Text(
            text = "当前共有 $taskCount 个团队任务",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant  // 次要文本颜色（灰色调）
        )
    }
}

/**
 * TaskCard —— 任务卡片组件。
 *
 * 以 Material3 Card 容器展示单个任务的摘要信息，包含：
 * - **第一行**（水平分布）：任务标题（左） + 状态标签（右）
 * - **第二行**（水平分布）：负责人（左） + 截止日期（右）
 *
 * 整张卡片可点击，点击事件上抛给调用方处理（通常用于导航到任务详情页）。
 *
 * @param task 要展示的任务数据模型
 * @param onClick 卡片点击回调（由 TaskListContent 传入，携带该任务的 ID）
 */
@Composable
private fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    /**
     * Card —— Material3 卡片容器。
     * - clickable：使整个卡片区域可点击
     * - surface 背景色：与页面背景形成视觉层级
     * - elevation：小号阴影（由 Theme 定义），增加卡片立体感和视觉权重
     */
    Card(
        modifier = Modifier.fillMaxSize().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = TeamTaskTheme.spacing.cardElevationSmall
        )
    ) {
        // 卡片内边距（大号），使内容不紧贴卡片边缘
        Column(
            modifier = Modifier.fillMaxSize().padding(TeamTaskTheme.spacing.large)
        ) {
            // ==================== 第一行：标题 + 状态标签 ====================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,        // 顶部对齐，适配多行标题
                horizontalArrangement = Arrangement.SpaceBetween  // 左右分布
            ) {
                /**
                 * 任务标题。
                 * - weight(1f)：占据剩余空间，确保状态标签始终靠右对齐
                 * - maxLines = 2：最多显示两行，超出以省略号截断
                 * - 使用 titleMedium 样式，半粗体
                 */
                Text(
                    text = task.title,
                    modifier = Modifier.weight(1f).padding(end = TeamTaskTheme.spacing.medium),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // 任务状态标签（TODO / IN_PROGRESS / DONE / BLOCKED）
                TaskStatusBadge(status = task.status)
            }

            // 第一行与第二行之间的间距（中号）
            Spacer(modifier = Modifier.height(TeamTaskTheme.spacing.medium))

            // ==================== 第二行：负责人 + 截止日期 ====================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween  // 左右分布
            ) {
                /**
                 * 负责人信息。
                 * 通过 task.assignee.name 获取用户名称。
                 * bodySmall 样式，使用次强调色。
                 * 单行显示，超出截断。
                 */
                Text(
                    text = "负责人: ${task.assignee.name}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                /**
                 * 截止日期。
                 * 直接显示 task.dueDate 字符串（由数据层格式化）。
                 * bodySmall 样式，单行不截断。
                 */
                Text(
                    text = "截止: ${task.dueDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * TaskStatusBadge —— 任务状态标签徽章。
 *
 * 在任务的卡片右上角以彩色 Surface 容器展示任务状态文本。
 * 不同状态映射不同的 Material3 容器色和内容色：
 *
 * | 状态         | 容器色                     | 文本色                       |
 * |-------------|---------------------------|-----------------------------|
 * | TODO        | secondaryContainer        | onSecondaryContainer        |
 * | IN_PROGRESS | primaryContainer          | onPrimaryContainer          |
 * | DONE        | tertiaryContainer         | onTertiaryContainer         |
 * | BLOCKED     | errorContainer            | onErrorContainer            |
 *
 * 通过 MaterialTheme.colorScheme 的语义色实现，在切换亮/暗主题时自动适配。
 *
 * @param status 任务状态枚举值，决定标签的颜色方案和显示文本
 */
@Composable
private fun TaskStatusBadge(
    status: TaskStatus
) {
    // ==================== 状态 → 颜色映射 ====================

    /**
     * 容器背景色：使用 Material 语义容器色。
     * 每种状态对应不同的色素（secondary/primary/tertiary/error），
     * 通过视觉颜色直观区分任务当前阶段。
     */
    val containerColor = when (status) {
        TaskStatus.TODO -> MaterialTheme.colorScheme.secondaryContainer
        TaskStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer
        TaskStatus.DONE -> MaterialTheme.colorScheme.tertiaryContainer
        TaskStatus.BLOCKED -> MaterialTheme.colorScheme.errorContainer
    }

    /**
     * 文本/内容色：与容器色配对的 on 色，
     * 保证在对应容器色上有足够对比度，符合无障碍标准。
     */
    val contentColor = when (status) {
        TaskStatus.TODO -> MaterialTheme.colorScheme.onSecondaryContainer
        TaskStatus.IN_PROGRESS -> MaterialTheme.colorScheme.onPrimaryContainer
        TaskStatus.DONE -> MaterialTheme.colorScheme.onTertiaryContainer
        TaskStatus.BLOCKED -> MaterialTheme.colorScheme.onErrorContainer
    }

    // ==================== 标签外观 ====================

    /**
     * Surface 容器：
     * - widthIn(min = 56.dp)：设置最小宽度，保证标签间距统一
     * - shape.small：小号圆角
     * - color / contentColor：根据状态动态设置
     */
    Surface(
        modifier = Modifier.widthIn(min = 56.dp),
        shape = MaterialTheme.shapes.small,
        color = containerColor,
        contentColor = contentColor
    ) {
        /**
         * 状态文本：
         * - status.toDisplayText()：通过扩展函数将枚举值转为可读的中文文本
         *   例如：TODO → "待办", IN_PROGRESS → "进行中"
         * - labelSmall：小号标签文本样式
         * - Medium 字重：在小型文本中保持可读性
         */
        Text(
            text = status.toDisplayText(),
            modifier = Modifier.padding(
                horizontal = TeamTaskTheme.spacing.small,      // 左右内边距（小号）
                vertical = TeamTaskTheme.spacing.extraSmall    // 上下内边距（极小号）
            ),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}