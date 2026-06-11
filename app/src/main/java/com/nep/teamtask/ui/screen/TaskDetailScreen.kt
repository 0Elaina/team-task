/**
 * 任务详情页 —— 展示单个任务的完整信息。
 *
 * 根据 taskId 从仓库加载数据，通过 UiState 驱动四种视图（加载/空/错误/内容）。
 * 顶部栏含返回按钮用于导航。
 */
package com.nep.teamtask.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.nep.teamtask.data.fake.FakeTaskRepository
import com.nep.teamtask.data.fake.FakeTaskResult
import com.nep.teamtask.data.model.Task
import com.nep.teamtask.ui.component.AssigneeInfo
import com.nep.teamtask.ui.component.DueDateInfo
import com.nep.teamtask.ui.component.EmptyView
import com.nep.teamtask.ui.component.ErrorView
import com.nep.teamtask.ui.component.LoadingView
import com.nep.teamtask.ui.component.StatusChip
import com.nep.teamtask.ui.component.TaskInfoRow
import com.nep.teamtask.ui.state.UiState
import com.nep.teamtask.ui.theme.TeamTaskTheme

/**
 * @param taskId 要展示的任务 ID，驱动数据加载
 * @param onBackClick 点击返回按钮的回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    onBackClick: () -> Unit
) {
    var taskUiState by remember(taskId) {
        mutableStateOf<UiState<Task>>(UiState.Loading)
    }

    var reloadKey by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(taskId, reloadKey) {
        taskUiState = UiState.Loading

        taskUiState = when (val result = FakeTaskRepository.getTaskById(taskId)) {
            is FakeTaskResult.Success -> UiState.Success(result.data)
            is FakeTaskResult.Failure -> UiState.Error(result.message)
            is FakeTaskResult.NoPermission -> UiState.Error(result.message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "任务详情") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = taskUiState) {
            UiState.Loading -> {
                LoadingView(
                    modifier = Modifier.padding(innerPadding),
                    message = "正在加载任务详情..."
                )
            }

            UiState.Empty -> {
                EmptyView(
                    modifier = Modifier.padding(innerPadding),
                    title = "任务不存在",
                    description = "没有找到对应的任务详情",
                    actionText = "返回",
                    onActionClick = onBackClick
                )
            }

            is UiState.Error -> {
                ErrorView(
                    modifier = Modifier.padding(innerPadding),
                    title = "无法加载任务详情",
                    message = state.message,
                    retryText = "重新加载",
                    onRetryClick = { reloadKey++ }
                )
            }

            is UiState.Success -> {
                TaskDetailContent(
                    modifier = Modifier.padding(innerPadding),
                    task = state.data
                )
            }
        }
    }
}

/**
 * TaskDetailContent —— 任务详情主体内容。
 * 可滚动的垂直布局：标题区域 → 描述卡片 → 属性卡片。
 */
@Composable
private fun TaskDetailContent(
    task: Task,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(TeamTaskTheme.spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(TeamTaskTheme.spacing.large)
    ) {
        // 标题: 任务名称 + ID
        Column(
            verticalArrangement = Arrangement.spacedBy(TeamTaskTheme.spacing.small)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "任务 ID: ${task.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 描述卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = TeamTaskTheme.spacing.cardElevationSmall
            )
        ) {
            Column(
                modifier = Modifier.padding(TeamTaskTheme.spacing.large),
                verticalArrangement = Arrangement.spacedBy(TeamTaskTheme.spacing.medium)
            ) {
                Text(
                    text = "描述",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 属性卡片：状态、负责人、截止日期
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = TeamTaskTheme.spacing.cardElevationSmall
            )
        ) {
            Column(
                modifier = Modifier.padding(TeamTaskTheme.spacing.large)
            ) {
                TaskInfoRow(label = "状态"){
                    StatusChip(status = task.status)
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = TeamTaskTheme.spacing.medium)
                )

                TaskInfoRow(label = "负责人") {
                    AssigneeInfo(user = task.assignee)
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = TeamTaskTheme.spacing.medium)
                )

                TaskInfoRow(label = "截止日期") {
                    DueDateInfo(dueDate = task.dueDate)
                }
            }
        }
    }
}

