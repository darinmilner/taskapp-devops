package com.taskapidemo.taskapp.data

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "task", uniqueConstraints = [UniqueConstraint(name = "task_description", columnNames = ["description"])])
class Task {
    @Id
    @GeneratedValue(generator = "task_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_sequence", sequenceName = "task_sequence", allocationSize = 1)
    val id: Long = 0

    @NotBlank
    @Column(name = "description", nullable = false, unique = true)
    var description: String = ""

    @Column(name = "is_reminder_set", nullable = false)
    var isReminderSet: Boolean = false

    @Column(name = "is_task_open", nullable = false)
    var isTaskOpen: Boolean = true

    @NotNull
    @Enumerated(EnumType.STRING)
    val priority: Priority = Priority.LOW

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}