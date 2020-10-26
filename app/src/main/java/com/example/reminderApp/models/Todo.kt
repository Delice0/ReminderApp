package com.example.reminderApp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todo_table")
data class Todo(
    var title: String,
    var description: String,
    val priority: String,
    @ColumnInfo(name = "due_date")
    val dueDate: LocalDateTime,
    @ColumnInfo(name = "created_date")
    val createdDate: LocalDateTime
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todoId")
    var id: Long? = null
    @ColumnInfo(name = "is_done")
    var isDone: Boolean = false
    @ColumnInfo(name = "done_date")
    var doneDate: LocalDateTime? = null
    override fun toString(): String {
        return "Todo with parameters: {\n" +
                "\tID: $id\n" +
                "\tTitle: $title\n" +
                "\tDescription: $description\n" +
                "\tPriority: $priority\n" +
                "\tDueDate: $dueDate\n" +
                "\tcreatedDate: $createdDate\n" +
                "\tIsDone: $isDone\n" +
                "\tDoneDate: $doneDate\n" +
                "}"
    }
}