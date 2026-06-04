package com.nep.teamtask.ui.navigation

import android.net.Uri

/**
 * 应用导航路由定义。
 *
 * 集中管理所有页面路由路径和参数名称，避免在导航代码中硬编码字符串。
 * 路由分为两类：
 * - **静态路由**：无需参数的页面（如首页、创建任务、个人中心），直接使用常量值跳转。
 * - **动态路由**：需要参数的页面（如任务详情），通过模板和构造函数生成完整路径。
 *
 * 使用示例：
 * ```
 * // 跳转静态路由
 * navController.navigate(TeamTaskRoutes.HOME)
 *
 * // 跳转动态路由
 * navController.navigate(TeamTaskRoutes.taskDetail(taskId = "123"))
 * ```
 */
object TeamTaskRoutes {

    /** 首页路由。应用主页面，展示任务列表。 */
    const val HOME = "home"

    /** 创建任务页面路由。用于创建新任务。 */
    const val CREATE_TASK = "create_task"

    /** 个人中心页面路由。显示用户个人信息与设置。 */
    const val PROFILE = "profile"

    /**
     * 任务 ID 参数名称。
     *
     * 用于在路由模板 [TASK_DETAIL] 中作为占位符，以及在 NavBackStackEntry
     * 中通过该键名提取 [TASK_DETAIL] 页面所需的 taskId 参数值。
     */
    const val ARG_TASK_ID = "taskId"

    /**
     * 任务详情页路由模板。
     *
     * 模板格式：`task_detail/{taskId}`
     * - 用于在 NavHost 中通过 `composable` 注册路由。
     * - 跳转时请使用 [taskDetail] 构造函数生成包含实际参数值的路径。
     *
     * 注册示例：
     * ```
     * composable(
     *     route = TeamTaskRoutes.TASK_DETAIL,
     *     arguments = listOf(navArgument(TeamTaskRoutes.ARG_TASK_ID) { type = NavType.StringType })
     * ) { backStackEntry ->
     *     val taskId = backStackEntry.arguments?.getString(TeamTaskRoutes.ARG_TASK_ID)
     *     TaskDetailScreen(taskId = taskId)
     * }
     * ```
     */
    const val TASK_DETAIL = "task_detail/{$ARG_TASK_ID}"

    /**
     * 构建任务详情页的完整导航路径。
     *
     * 对传入的 [taskId] 进行 URI 编码，确保路径中的特殊字符（如空格、中文等）
     * 能被正确解析，避免导航因非法字符而失败。
     *
     * @param taskId 任务唯一标识符。
     * @return 编码后的完整路由路径，格式为 `task_detail/{uriEncodedTaskId}`。
     */
    fun taskDetail(taskId: String): String {
        return "task_detail/${Uri.encode(taskId)}"
    }
}