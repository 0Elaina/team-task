package com.nep.teamtask.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nep.teamtask.ui.theme.TeamTaskTheme

/**
 * 全屏加载占位组件。
 *
 * 用于在异步数据加载过程中展示一个居中的加载指示器和提示文本，
 * 使用 [TeamTaskTheme] 中定义的自定义间距系统确保与整体设计规范一致。
 *
 * @param modifier 应用于根布局的 [Modifier]，默认值为 [Modifier]（无额外修饰）
 * @param message 显示在加载指示器下方的提示文本，默认值为 "正在加载..."
 *
 * 使用示例：
 * ```
 * // 默认消息
 * LoadingView()
 *
 * // 自定义消息
 * LoadingView(message = "加载任务列表中...")
 *
 * // 结合父级约束
 * LoadingView(modifier = Modifier.background(Color.White))
 * ```
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String = "正在加载..."
) {
    Column(
        modifier = modifier.fillMaxSize().padding(TeamTaskTheme.spacing.extraLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = message,
            modifier = Modifier.padding(top = TeamTaskTheme.spacing.large),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}