package com.taskapidemo.taskapp.services

import com.taskapidemo.taskapp.data.Task
import com.taskapidemo.taskapp.exceptions.BadRequestException
import com.taskapidemo.taskapp.exceptions.TaskNotFoundException
import com.taskapidemo.taskapp.models.TaskCreateRequest
import com.taskapidemo.taskapp.models.TaskDto
import com.taskapidemo.taskapp.models.TaskUpdateRequest
import com.taskapidemo.taskapp.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.stream.Collectors
import kotlin.reflect.full.memberProperties

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

    fun createTask(request: TaskCreateRequest): TaskDto {
        if (repository.doesDescriptionExist(request.description)) {
            throw BadRequestException("Task already exists with this description.")
        }

        val task = Task()
        mapFromRequestToEntity(task, request)
        val savedTask: Task = repository.save(task)
        return mappingEntityToDto(savedTask)
    }

    fun updateTask(id: Long, request: TaskUpdateRequest): TaskDto {
        findTaskById(id)
        val exisitingTask: Task = repository.findTaskById(id)

        for (prop in TaskUpdateRequest::class.memberProperties) {
            if (prop.get(request) != null) {
                val field: Field? = ReflectionUtils.findField(Task::class.java, prop.name)
                field?.let {
                    it.isAccessible = true
                    ReflectionUtils.setField(it, exisitingTask, prop.get(request))
                }
            }
        }

        val updatedTask: Task = repository.save(exisitingTask)
        return mappingEntityToDto(updatedTask)
    }
}