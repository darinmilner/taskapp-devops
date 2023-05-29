package com.taskapidemo.taskapp.models

import com.taskapidemo.taskapp.data.Priority
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class TaskUpdateRequest(
    val description: String?,
    val isReminderSet: Boolean?,
    val isTaskOpen: Boolean?,
    val priority: Priority?,
    @NotBlank(message = "Task updatedAt can not be empty")
    val updatedAt: LocalDateTime
)
