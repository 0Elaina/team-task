package com.nep.teamtask.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.nep.teamtask.ui.theme.TeamTaskTheme

/**
 * 错误状态占位组件。
 *
 * 当页面加载失败或发生异常时，使用此组件向用户展示错误信息。
 * 包含一个警告图标（使用 [MaterialTheme.colorScheme.error] 着色）、标题、描述消息以及一个重试按钮，
 * 与 [EmptyView] 不同，该组件的重试按钮始终可见且点击回调为必传参数。
 *
 * @param modifier 应用于根布局的 [Modifier]，默认值为 [Modifier]
 * @param title 错误主标题，默认值为 "加载失败"
 * @param message 错误详细描述，默认值为 "页面加载失败, 请稍后重试"
 * @param retryText 重试按钮上显示的文本，默认值为 "重试"
 * @param onRetryClick 重试按钮点击回调，用于触发重新加载逻辑
 *
 * 使用示例：
 * ```
 * // 使用默认文案
 * ErrorView(onRetryClick = { viewModel.reload() })
 *
 * // 自定义错误信息
 * ErrorView(
 *     title = "网络异常",
 *     message = "无法连接到服务器，请检查网络设置后重试",
 *     retryText = "重新加载",
 *     onRetryClick = { viewModel.reload() }
 * )
 *
 * // 根据异常类型动态展示
 * ErrorView(
 *     message = error.message ?: "未知错误",
 *     onRetryClick = { viewModel.retry() }
 * )
 * ```
 */
@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    title: String = "加载失败",
    message: String = "页面加载失败, 请稍后重试",
    retryText: String = "重试",
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(TeamTaskTheme.spacing.extraLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(TeamTaskTheme.spacing.contentPadding),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(TeamTaskTheme.spacing.large))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(TeamTaskTheme.spacing.small))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(TeamTaskTheme.spacing.large))

        Button(
            onClick = onRetryClick
        ) {
            Text(text = retryText)
        }
    }
}