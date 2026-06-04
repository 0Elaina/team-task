package com.nep.teamtask.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nep.teamtask.ui.screen.CreateTaskScreen
import com.nep.teamtask.ui.screen.HomeScreen
import com.nep.teamtask.ui.screen.ProfileScreen
import com.nep.teamtask.ui.screen.TaskDetailScreen

/**
 * 应用主界面组件。
 *
 * 整合 [Scaffold] 布局与底部导航栏，并委托 [TeamTaskNavGraph] 管理页面路由。
 * 当当前页面属于应显示底部栏的路由（首页、个人中心）时渲染 [TeamTaskBottomBar]。
 *
 * @param modifier 应用于 Scaffold 的修饰符。
 * @param navController 导航控制器。默认通过 [rememberNavController] 创建，
 *                       若需从外部控制导航可传入共享实例。
 */
@Composable
fun TeamTaskNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentDestination.shouldShowBottomBar()) {
                TeamTaskBottomBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        TeamTaskNavGraph(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

/**
 * 导航图。
 *
 * 集中定义所有页面的路由与对应 Composable 之间的映射关系，接收父级 Scaffold
 * 的 [innerPadding] 以正确避开底部导航栏等系统栏区域。
 *
 * 包含以下页面路由：
 * - [TeamTaskRoutes.HOME]：首页，启动目标页面（startDestination）。
 * - [TeamTaskRoutes.TASK_DETAIL]：任务详情页，接收 `taskId` 参数。
 * - [TeamTaskRoutes.CREATE_TASK]：创建任务页。
 *
 * @param navController 导航控制器。
 * @param innerPadding 父级 Scaffold 提供的内边距，用于避开系统栏与底部导航栏。
 */
@Composable
private fun TeamTaskNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = TeamTaskRoutes.HOME,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = TeamTaskRoutes.HOME) {
            HomeScreen(
                onTaskClick = { taskId ->
                    navController.navigate(TeamTaskRoutes.taskDetail(taskId))
                },
                onCreateTaskClick = {
                    navController.navigate(TeamTaskRoutes.CREATE_TASK)
                }
            )
        }

        composable(
            route = TeamTaskRoutes.TASK_DETAIL,
            arguments = listOf(
                navArgument(TeamTaskRoutes.ARG_TASK_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments
                ?.getString(TeamTaskRoutes.ARG_TASK_ID)
                .orEmpty()

            TaskDetailScreen(
                taskId = taskId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = TeamTaskRoutes.CREATE_TASK) {
            CreateTaskScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTaskCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = TeamTaskRoutes.PROFILE) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * 判断当前路由是否需要显示底部导航栏。
 *
 * 底部导航栏仅在一级页面（首页、个人中心）中显示，
 * 在详情页、创建任务页等二级页面中隐藏。
 *
 * @return 如果当前路由为 [TeamTaskRoutes.HOME] 或 [TeamTaskRoutes.PROFILE] 返回 true，否则返回 false。
 */
private fun NavDestination?.shouldShowBottomBar(): Boolean {
    val route = this?.route

    return route == TeamTaskRoutes.HOME || route == TeamTaskRoutes.PROFILE
}