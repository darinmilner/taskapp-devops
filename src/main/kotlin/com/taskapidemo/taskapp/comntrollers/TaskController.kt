package com.taskapidemo.taskapp.comntrollers

import com.taskapidemo.taskapp.models.TaskCreateRequest
import com.taskapidemo.taskapp.models.TaskDto
import com.taskapidemo.taskapp.models.TaskUpdateRequest
import com.taskapidemo.taskapp.services.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api")
class TaskController(private val service: TaskService) {
    @GetMapping("all-tasks")
    fun getAllTasks(): ResponseEntity<List<TaskDto>> {
        return ResponseEntity(service.getAllTasks(), HttpStatus.OK)
    }

    @GetMapping("all-open-tasks")
    fun getAllOpenTasks(): ResponseEntity<List<TaskDto>> {
        return ResponseEntity(service.getAllOpenTasks(), HttpStatus.OK)
    }

    @GetMapping("all-closed-tasks")
    fun getAllClosedTasks(): ResponseEntity<List<TaskDto>> {
        return ResponseEntity(service.getAllClosedTasks(), HttpStatus.OK)
    }

    @GetMapping("task/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskDto> =
        ResponseEntity(service.getTaskById(id), HttpStatus.OK)

    @PostMapping("create")
    fun createTask(@Valid @RequestBody request: TaskCreateRequest): ResponseEntity<TaskDto> {
        return ResponseEntity(service.createTask(request), HttpStatus.CREATED)
    }

    @PatchMapping("update/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody request: TaskUpdateRequest): ResponseEntity<TaskDto> =
        ResponseEntity(service.updateTask(id, request), HttpStatus.OK)

    @DeleteMapping("delete/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<String> =
        ResponseEntity(service.deleteTask(id), HttpStatus.OK)
}