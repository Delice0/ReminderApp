package com.example.reminderApp.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todo_table")
data class Todo(
    val title: String,
    val description: String,
    val priority: Int,
    @ColumnInfo(name = "due_date")
    val dueDate: LocalDateTime,
    @ColumnInfo(name = "created_date")
    val createdDate: LocalDateTime
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todoId")
    var id: Long = 0
    @ColumnInfo(name = "is_done")
    var isDone: Boolean = false
    @ColumnInfo(name = "done_date")
    var doneDate: LocalDateTime? = null
}