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
 * 个人中心页。
 *
 * 展示用户信息和设置入口，当前为原型实现——显示占位文字和返回按钮。
 *
 * @param onBackClick 点击"返回"时的回调。
 */
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(TeamTaskTheme.spacing.screenPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "个人页",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "用户信息 / 设置入口",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = onBackClick) {
            Text(text = "返回")
        }
    }
}