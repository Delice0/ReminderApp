package com.example.reminderApp.repositories

import androidx.lifecycle.LiveData
import com.example.reminderApp.daos.TodoDao
import com.example.reminderApp.models.Todo

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()
    val allDoneTodos: LiveData<List<Todo>> = todoDao.getAllDoneTodos()

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    fun delete(position: Int) {
        allTodos.value?.get(position)?.let { todoDao.delete(it) }
    }

    suspend fun finish(position: Int) {
        allTodos.value?.get(position)?.let {
            todoDao.finish(it)
        }
    }

    suspend fun update(id: Long) {
        allTodos.value?.stream()
            ?.filter { it.id == id }
            ?.findFirst()
            ?.get()
            ?.let { todoDao.update(it) }
    }

    fun deleteDoneTodos() {
        todoDao.deleteDoneTodos()
    }
}