package com.taskapidemo.taskapp.comntrollers

import com.taskapidemo.taskapp.models.TaskDto
import com.taskapidemo.taskapp.services.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}