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
 * 创建任务页。
 *
 * 提供创建新任务的入口，当前为原型实现——包含"创建任务"和"返回"按钮。
 * 创建成功后通过 [onTaskCreated] 回调通知调用方。
 *
 * @param onBackClick 点击"返回"时的回调。
 * @param onTaskCreated 任务创建成功后的回调。
 */
@Composable
fun CreateTaskScreen(
    onBackClick: () -> Unit,
    onTaskCreated: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(TeamTaskTheme.spacing.screenPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "创建任务页",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Button(onClick = onTaskCreated) {
            Text(text = "创建任务")
        }

        Button(onClick = onBackClick) {
            Text(text = "返回")
        }
    }
}