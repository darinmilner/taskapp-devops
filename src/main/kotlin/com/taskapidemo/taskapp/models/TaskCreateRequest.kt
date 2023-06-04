package com.taskapidemo.taskapp.models

import com.taskapidemo.taskapp.data.Priority
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

// TODO - createdAt and updatedAt should be automatically set
data class TaskCreateRequest(
    @NotBlank(message = "Task description can not be empty")
    val description: String,

    val isReminderSet: Boolean,

    val isTaskOpen: Boolean,

    val priority: Priority,

    @NotBlank(message = "Task createdAt can not be empty")
    val createdAt: LocalDateTime,

    @NotBlank(message = "Task updatedAt can not be empty")
    val updatedAt: LocalDateTime
)
