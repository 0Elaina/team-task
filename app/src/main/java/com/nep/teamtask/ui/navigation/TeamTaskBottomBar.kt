package com.nep.teamtask.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

/**
 * 底部导航栏组件。
 *
 * 使用 [teamTaskBottomNavItems] 定义的项列表渲染 [NavigationBar]，
 * 根据当前导航目的地 [currentDestination] 高亮对应项，并通过 [navController]
 * 处理页面切换与返回栈管理。
 *
 * @param navController 导航控制器，用于执行页面跳转操作。
 * @param currentDestination 当前导航目的地，用于判断哪一项处于选中状态。
 *                           通常通过 `NavBackStackEntry` 的 `destination` 属性获取。
 */
@Composable
fun TeamTaskBottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        teamTaskBottomNavItems.forEach { item ->
            // 判断当前导航项是否处于选中状态
            // 通过比较当前导航目的地与导航项的路由是否匹配来确定
            val selected = currentDestination
                ?.hierarchy                              // 获取当前目的地的导航层级链（包含当前节点及其所有父节点）
                ?.any { destination ->    // 遍历层级链，检查是否有任何节点的路由与当前导航项匹配
                    destination.route == item.route      // 比较路由字符串，判断是否为同一个页面
                } == true                                // 将匹配结果转换为布尔值：找到匹配则为true（选中），否则为false

            NavigationBarItem(
                selected = selected,
                onClick = {
                    // 执行导航跳转到目标路由
                    navController.navigate(item.route) {
                        // 配置返回栈行为：弹出到HOME路由（包含HOME）
                        // 这会清除HOME之上的所有页面，确保不会重复堆积相同页面
                        popUpTo(TeamTaskRoutes.HOME) {
                            saveState = true  // 保存被弹出页面的状态（如滚动位置、输入内容等）
                        }

                        launchSingleTop = true  // 如果目标路由已在返回栈顶部，则不创建新实例，避免重复跳转
                        
                        restoreState = true     // 恢复之前保存的页面状态（与saveState配合使用）
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(text = item.label)
                }
            )

        }
    }
}