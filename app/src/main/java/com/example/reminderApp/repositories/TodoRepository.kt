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

    fun delete(todo: Todo) {
        Timber.i("Deleting from database - ${allTodos.value!!.stream().filter { it == todo }.findFirst().get()}")
        todoDao.delete(todo)
    }

    suspend fun finish(id: Long) {
        Timber.i("Setting todo to finish - ${allTodos.value!!.stream().filter { it.id == id }.findFirst().get()}")
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