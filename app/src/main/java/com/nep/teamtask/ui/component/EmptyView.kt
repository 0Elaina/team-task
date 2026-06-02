package com.nep.teamtask.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
 * 空状态占位组件。
 *
 * 当列表或页面无数据可展示时，使用此组件向用户提供友好的反馈。
 * 包含一个图标、标题、描述文本，并可选择性展示一个操作按钮供用户手动重试或其他操作。
 *
 * @param modifier 应用于根布局的 [Modifier]，默认值为 [Modifier]
 * @param title 空状态主标题，默认值为 "暂无数据"
 * @param description 空状态补充描述，默认值为 "当前没有可展示的内容"
 * @param actionText 操作按钮上显示的文本，为 `null` 时不展示按钮
 * @param onActionClick 操作按钮点击回调，为 `null` 时不展示按钮
 *
 * 使用示例：
 * ```
 * // 仅展示默认提示
 * EmptyView()
 *
 * // 自定义标题与描述
 * EmptyView(
 *     title = "任务列表为空",
 *     description = "点击下方按钮创建第一个任务"
 * )
 *
 * // 带操作按钮
 * EmptyView(
 *     title = "加载失败",
 *     description = "请检查网络后重试",
 *     actionText = "重试",
 *     onActionClick = { viewModel.reload() }
 * )
 * ```
 */
@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    title: String = "暂无数据",
    description: String = "当前没有可展示的内容",
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize().padding(TeamTaskTheme.spacing.extraLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(TeamTaskTheme.spacing.contentPadding),
            tint = MaterialTheme.colorScheme.primary
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
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if(actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(TeamTaskTheme.spacing.large))

            Button(
                onClick = onActionClick
            ) {
                Text(text = actionText)
            }
        }
    }
}