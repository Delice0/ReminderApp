package com.example.reminderApp.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reminderApp.DAOs.TodoDao
import com.example.reminderApp.Models.Todo

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodosByPriority()
    val allDoneTodos: LiveData<List<Todo>> = todoDao.getAllDoneTodos()

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    fun delete(position: Int) {
        allTodos.value?.get(position)?.let { todoDao.delete(it) }
    }

    suspend fun update(position: Int) {
        allTodos.value?.get(position)?.let {
            todoDao.update(it)
        }
    }

}