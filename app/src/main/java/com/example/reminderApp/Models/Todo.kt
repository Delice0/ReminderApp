package com.example.reminderApp.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    val title: String,
    val description: String,
    val priority: String,
    @ColumnInfo(name = "due_date")
    val dueDate: String,
    @ColumnInfo(name = "created_date")
    val createdDate: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todoId")
    var id: Long = 0
    @ColumnInfo(name = "is_done")
    var isDone: Boolean = false
    @ColumnInfo(name = "done_date")
    var doneDate: String = ""
}