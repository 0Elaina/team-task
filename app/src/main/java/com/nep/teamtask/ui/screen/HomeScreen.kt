package com.nep.teamtask.ui.screen

import android.R.attr.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nep.teamtask.ui.theme.TeamTaskTheme

/**
 * 首页。
 *
 * 应用主页面，提供导航入口：
 * - 查看任务详情（使用示例 ID "task-001" 跳转）
 * - 前往创建任务页
 *
 * @param onTaskClick 点击"查看任务详情"时的回调，参数为任务 ID。
 * @param onCreateTaskClick 点击"创建任务"时的回调。
 */
@Composable
fun HomeScreen(
    onTaskClick: (String) -> Unit,
    onCreateTaskClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(TeamTaskTheme.spacing.screenPadding),
        verticalArrangement = Arrangement.Center // 排列方式: 垂直居中
    ) {
        Text(
            text = "TeamTask",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "团队任务协作原型",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = {
                onTaskClick("task-001")
            }
        ) {
            Text(text = "查看任务详情")
        }

        Button(onClick = onCreateTaskClick) {
            Text(text = "创建任务")
        }
    }
}