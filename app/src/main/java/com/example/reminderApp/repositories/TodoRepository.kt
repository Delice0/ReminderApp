package com.example.reminderApp.repositories

import androidx.lifecycle.LiveData
import com.example.reminderApp.daos.TodoDao
import com.example.reminderApp.models.Todo
import java.time.LocalDateTime

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()
    val allDoneTodos: LiveData<List<Todo>> = todoDao.getAllDoneTodos()

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    fun delete(position: Int) {
        allTodos.value!![position].let { todoDao.delete(it) }
    }

    suspend fun finish(id: Long) {
        todoDao.finish(id)
    }

    suspend fun update(id: Long) {
        allTodos.value!!.stream()
            .filter { it.id == id }
            .findFirst()
            .get()
            .let { todoDao.update(it) }
    }

    fun deleteDoneTodos() {
        todoDao.deleteDoneTodos()
    }
}