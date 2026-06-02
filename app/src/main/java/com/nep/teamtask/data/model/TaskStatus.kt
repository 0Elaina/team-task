package com.nep.teamtask.data.model

enum class TaskStatus{
    TODO,           // 待处理
    IN_PROGRESS,    // 进行中
    DONE,           // 已完成
    BLOCKED         // 被阻塞
}

fun TaskStatus.toDisplayText(): String {
    return when (this) {
        TaskStatus.TODO -> "待处理"
        TaskStatus.IN_PROGRESS -> "进行中"
        TaskStatus.DONE -> "已完成"
        TaskStatus.BLOCKED -> "被阻塞"
    }
}