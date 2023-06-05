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
}