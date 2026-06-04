package com.nep.teamtask.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nep.teamtask.ui.theme.TeamTaskTheme

/**
 * 任务详情页。
 *
 * 展示指定任务的详细信息，当前为原型实现——显示任务 ID 和返回按钮。
 *
 * @param taskId 要查看的任务唯一标识符。
 * @param onBackClick 点击"返回"时的回调，通常导航回上一页。
 */
@Composable
fun TaskDetailScreen(
    taskId: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(TeamTaskTheme.spacing.screenPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "任务详情页",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "当前任务 ID: $taskId",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = onBackClick) {
            Text(text = "返回")
        }
    }
}