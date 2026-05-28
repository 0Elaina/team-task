package com.nep.teamtask.data.model

data class Task(
    val id: Int, // 任务 id
    val title: String, // 任务标题
    val description: String, // 描述
    val status: TaskStatus, // 状态
    val assignee: String, // 分配给谁
    val dueDate: String // 截止日期
)