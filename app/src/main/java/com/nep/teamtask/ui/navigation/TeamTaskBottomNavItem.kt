package com.nep.teamtask.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 底部导航栏项的数据模型。
 *
 * 定义底部导航栏中每一项所需的三个核心属性。
 *
 * @property route 导航目标路由，需与 [TeamTaskRoutes] 中定义的路由常量对应。
 * @property label 导航项显示的文本标签。
 * @property icon 导航项显示的图标。
 */
data class TeamTaskBottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

/** 底部导航栏所有项的定义列表。当前包含首页（Tasks）和个人中心（Profile）两项。 */
val teamTaskBottomNavItems = listOf(
    TeamTaskBottomNavItem(
        route = TeamTaskRoutes.HOME,
        label = "Tasks",
        icon = Icons.Filled.Checklist
    ),
    TeamTaskBottomNavItem(
        route = TeamTaskRoutes.PROFILE,
        label = "profile",
        icon = Icons.Filled.Person
    )
)