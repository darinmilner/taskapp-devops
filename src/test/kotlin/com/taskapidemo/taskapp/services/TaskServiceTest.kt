package com.taskapidemo.taskapp.services

import com.taskapidemo.taskapp.data.Priority
import com.taskapidemo.taskapp.data.Task
import com.taskapidemo.taskapp.models.TaskCreateRequest
import com.taskapidemo.taskapp.models.TaskDto
import com.taskapidemo.taskapp.repository.TaskRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class TaskServiceTest {
    @RelaxedMockK
    private lateinit var mockRepository: TaskRepository

    @InjectMockKs
    private lateinit var objectUnderTest: TaskService

    private val task = Task()
    private lateinit var createRequest: TaskCreateRequest

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createRequest = TaskCreateRequest(
            description = "test",
            isTaskOpen = false,
            isReminderSet = false,
            priority = Priority.LOW,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }

    @Test
    fun checkSizeIsCorrectWhenAllTasksGetFetched() {
        val expectedTasks: List<Task> = listOf(
            task,
            task
        )

        every { mockRepository.findAll() } returns expectedTasks.toMutableList()

        val actualTasks: List<TaskDto> = objectUnderTest.getAllTasks()

        assertThat(actualTasks.size).isEqualTo(expectedTasks.size)
    }
}