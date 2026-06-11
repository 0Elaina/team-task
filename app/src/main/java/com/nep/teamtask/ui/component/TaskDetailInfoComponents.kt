package com.nep.teamtask.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.nep.teamtask.data.model.TaskStatus
import com.nep.teamtask.data.model.User
import com.nep.teamtask.data.model.toDisplayText
import com.nep.teamtask.ui.theme.TeamTaskTheme

/**
 * 详情页通用信息行。
 *
 * 左侧展示字段名，右侧展示具体内容。
 * 右侧使用 slot 设计，方便放 Text、Chip、用户信息等不同类型内容。
 */
@Composable
fun TaskInfoRow(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.weight(1.6f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

/**
 * 任务状态标签。
 *
 * 使用不同的 Material colorScheme token 区分状态，
 * 但不直接写死具体颜色，方便后续适配深色模式和主题切换。
 */
@Composable
fun StatusChip(
    status: TaskStatus,
    modifier: Modifier = Modifier
) {
    val containerColor = when(status) {
        TaskStatus.TODO -> MaterialTheme.colorScheme.surfaceVariant
        TaskStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer
        TaskStatus.DONE -> MaterialTheme.colorScheme.secondaryContainer
        TaskStatus.BLOCKED -> MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = when(status) {
        TaskStatus.TODO -> MaterialTheme.colorScheme.onSurfaceVariant
        TaskStatus.IN_PROGRESS -> MaterialTheme.colorScheme.onPrimaryContainer
        TaskStatus.DONE -> MaterialTheme.colorScheme.onSecondaryContainer
        TaskStatus.BLOCKED -> MaterialTheme.colorScheme.onErrorContainer
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = TeamTaskTheme.spacing.medium,
                vertical = TeamTaskTheme.spacing.extraSmall
            ),
            text = status.toDisplayText(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * 负责人信息。
 *
 * 当前没有真实头像资源，所以先使用姓名首字母头像。
 * 后续如果 User.avatarUrl 接入真实图片，可以在这里统一替换为 AsyncImage。
 */
@Composable
fun AssigneeInfo(
    user: User,
    modifier: Modifier = Modifier
) {
    val avatarText = user.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(TeamTaskTheme.spacing.screenPadding),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = avatarText,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(TeamTaskTheme.spacing.small))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = user.role,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 截止日期信息。
 *
 * 先保持简单文本展示。
 * 后续可以扩展为：逾期高亮、今天/明天标签、日期选择器格式化等。
 */
@Composable
fun DueDateInfo(
    dueDate: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = TeamTaskTheme.spacing.medium,
                vertical = TeamTaskTheme.spacing.extraSmall
            ),
            text = dueDate,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
    }
}