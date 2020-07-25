package com.example.reminderApp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.reminderApp.Database.TodoRoomDatabase
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.Repositories.TodoRepository
import com.example.reminderApp.Utils.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.stream.Collector
import java.util.stream.Collectors

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository

    val allTodos: LiveData<List<Todo>>
    val allDoneTodos: LiveData<List<Todo>>

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

    fun update(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        setDone(position)
        repository.update(position)
    }

    private fun setDone(position: Int) {
        allTodos.value?.get(position).let {
            it?.doneDate = DateUtil.dateFormat(LocalDate.now())
            it?.isDone = true
        }
    }
}