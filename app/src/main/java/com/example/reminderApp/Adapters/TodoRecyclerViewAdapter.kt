package com.example.reminderApp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.ViewHolders.TodoViewHolder

class TodoRecyclerViewAdapter : RecyclerView.Adapter<TodoViewHolder>() {
    private lateinit var listener: TodoItemListener
    private var todos = emptyList<Todo>() // Cached todos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TodoViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.bind(currentTodo, listener)
    }

    internal fun setTodos(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

    fun itemClicked(listener: TodoItemListener) {
        this.listener = listener
    }
}