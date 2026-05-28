package com.nep.teamtask.data.fake

import com.nep.teamtask.data.model.Task
import com.nep.teamtask.data.model.TaskStatus

object FakeTaskRepository {
    fun getTasks(): List<Task> {
        return listOf(
            Task(
                id = 1,
                title = "设计任务列表页面",
                description = "完成首页任务卡片、状态标签和负责人展示。",
                status = TaskStatus.InProgress,
                assignee = "Noire",
                dueDate = "2026-06-01"
            ),
            Task(
                id = 2,
                title = "创建任务表单",
                description = "实现标题、描述、负责人、截止日期输入。",
                status = TaskStatus.Todo,
                assignee = "Vert",
                dueDate = "2026-06-03"
            ),
            Task(
                id = 3,
                title = "补充空态和错误态",
                description = "为任务列表增加 loading、empty、error UI。",
                status = TaskStatus.Todo,
                assignee = "Blanc",
                dueDate = "2026-06-05"
            )
        )
    }
}