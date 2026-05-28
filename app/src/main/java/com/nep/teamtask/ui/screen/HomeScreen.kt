package com.nep.teamtask.ui.screen

import android.R.attr.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nep.teamtask.ui.theme.TeamTaskTheme

@Composable
fun HomeScreen() {
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
    }
}