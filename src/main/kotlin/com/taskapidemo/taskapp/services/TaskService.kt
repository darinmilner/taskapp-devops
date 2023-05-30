package com.taskapidemo.taskapp.services

import com.taskapidemo.taskapp.data.Task
import com.taskapidemo.taskapp.exceptions.TaskNotFoundException
import com.taskapidemo.taskapp.models.TaskCreateRequest
import com.taskapidemo.taskapp.models.TaskDto
import com.taskapidemo.taskapp.repository.TaskRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TaskService(
    private val repository: TaskRepository
) {
    private fun mappingEntityToDto(task: Task): TaskDto {
        return TaskDto(
            id = task.id,
            description = task.description,
            isTaskOpen = task.isTaskOpen,
            priority = task.priority,
            isReminderSet = task.isReminderSet,
            createdAt = task.createdAt,
            updatedAt = task.updatedAt,
        )
    }

    private fun mapFromRequestToEntity(task: Task, request: TaskCreateRequest) {
        task.description = request.description
        task.isTaskOpen = request.isTaskOpen
        task.priority = request.priority
        task.isReminderSet = request.isReminderSet
    }

    private fun findTaskById(id: Long) {
        if (!repository.existsById(id)) {
            throw TaskNotFoundException("Task with $id not found.")
        }
    }

    fun getTaskById(id: Long): TaskDto {
        findTaskById(id)
        val task: Task = repository.findTaskById(id)
        return mappingEntityToDto(task)
    }

    fun getAllTasks() =
        repository.findAll().stream().map(this::mappingEntityToDto).collect(Collectors.toList())

    fun getAllOpenTasks() =
        repository.getAllOpenTasks().stream().map(this::mappingEntityToDto).collect(Collectors.toList())

    fun getAllClosedTasks() =
        repository.getAllClosedTasks().stream().map(this::mappingEntityToDto).collect(Collectors.toList())
}