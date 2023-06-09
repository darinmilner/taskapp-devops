package com.taskapidemo.taskapp.repository

import com.taskapidemo.taskapp.data.Task
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql

@DataJpaTest(properties = ["spring.jpa.properties.javax.persistence.validation.mode=none"])
class TaskRepositoryTest {
    @Autowired
    private lateinit var objectUnderTest: TaskRepository

    private val numberOfRecordsInTestInfoSQL = 3
    private val numberOfOpenRecordsInTestInfoSQL = 1
    private val numberOfClosedRecordsInTestInfoSQL = 2

    @Test
    @Sql("classpath:test-data.sql")
    fun whenTaskIsSavedCheckedForNotNull() {
        val task: Task = objectUnderTest.findTaskById(111)
        // then
        assertThat(task).isNotNull
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun checkNumberOfRecordsWhenAllTasksAreFetched() {
        val tasks: List<Task> = objectUnderTest.findAll()

        assertThat(tasks.size).isEqualTo(numberOfRecordsInTestInfoSQL)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun checkNumberOfRecordsAfterATaskIsDeleted() {
        objectUnderTest.deleteById(111)

        val tasks: List<Task> = objectUnderTest.findAll()

        assertThat(tasks.size).isEqualTo(numberOfRecordsInTestInfoSQL - 1)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun whenAllTasksAreQueriedCheckForCorrectNumberOpenTasks() {
        val tasks: List<Task> = objectUnderTest.getAllOpenTasks()

        assertThat(tasks.size).isEqualTo(numberOfOpenRecordsInTestInfoSQL)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun whenAllTasksAreQueriedCheckForCorrectNumberClosedTasks() {
        val tasks: List<Task> = objectUnderTest.getAllClosedTasks()

        assertThat(tasks.size).isEqualTo(numberOfClosedRecordsInTestInfoSQL)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun checkIfDescriptionAlreadyExists() {
        val descriptionExists1 = objectUnderTest.doesDescriptionExist("test description1")
        val descriptionExists2 = objectUnderTest.doesDescriptionExist("Test description that does not exist")

        assertThat(descriptionExists1).isTrue()
        assertThat(descriptionExists2).isFalse()
    }
}