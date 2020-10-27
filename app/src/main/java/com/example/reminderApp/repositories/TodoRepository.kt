package com.example.reminderApp.repositories

import androidx.lifecycle.LiveData
import com.example.reminderApp.daos.TodoDao
import com.example.reminderApp.models.Todo
import timber.log.Timber

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()
    val allDoneTodos: LiveData<List<Todo>> = todoDao.getAllDoneTodos()

    suspend fun insert(todo: Todo) {
        Timber.i("Inserting to database - $todo")
        todoDao.insert(todo)
    }

    fun delete(position: Int) {
        Timber.i("Deleting from database - ${allTodos.value!![position]}")
        allTodos.value!![position].let { todoDao.delete(it) }
    }

    suspend fun finish(id: Long) {
        Timber.i("Setting todo to finish - ${allTodos.value!![id.toInt()]}")
        todoDao.finish(id)
    }

    suspend fun update(todo: Todo) {
        Timber.i("Updating todo - $todo")
        todoDao.update(todo)
    }

    fun deleteDoneTodos() {
        Timber.i("Deleting all done todos - ${allDoneTodos.value!!}")
        todoDao.deleteDoneTodos()
    }
}