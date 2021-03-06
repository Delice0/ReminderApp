package com.example.reminderApp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reminderApp.database.TodoRoomDatabase
import com.example.reminderApp.models.Todo
import com.example.reminderApp.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository

    val allTodos: LiveData<List<Todo>>
    val allDoneTodos: LiveData<List<Todo>>
    val selected = MutableLiveData<Todo>()

    init {
        val todoDao = TodoRoomDatabase.getDatabase(application)
            .todoDao()

        repository = TodoRepository(todoDao)

        allTodos = repository.allTodos
        allDoneTodos = repository.allDoneTodos
    }

    fun select(todo: Todo) {
        selected.value = todo
    }

    fun insert(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(todo)
    }

    fun delete(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(todo)
    }

    fun finish(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.finish(id)
    }

    fun update(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(todo)
    }

    fun deleteAllDoneTodos() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteDoneTodos()
    }
}