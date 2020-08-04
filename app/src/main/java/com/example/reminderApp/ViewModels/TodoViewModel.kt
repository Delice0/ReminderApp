package com.example.reminderApp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.reminderApp.database.TodoRoomDatabase
import com.example.reminderApp.models.Todo
import com.example.reminderApp.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository

    val allTodos: LiveData<List<Todo>>
    val allDoneTodos: LiveData<List<Todo>>

    val selected = MutableLiveData<Todo>()

    fun select(todo: Todo) {
        selected.value = todo
    }

    init {
        val todoDao = TodoRoomDatabase.getDatabase(application, viewModelScope)
            .todoDao()

        repository = TodoRepository(todoDao)

        allTodos = repository.allTodos
        allDoneTodos = repository.allDoneTodos
    }

    fun insert(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(todo)
    }

    fun delete(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(position)
    }

    fun finish(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        updateTodo(position)
        repository.finish(position)
    }

    fun deleteAllDoneTodos() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteDoneTodos()
    }

    private fun updateTodo(position: Int) {
        allTodos.value?.get(position).let {
            it?.doneDate = LocalDateTime.now()
            it?.isDone = true
        }
    }
}