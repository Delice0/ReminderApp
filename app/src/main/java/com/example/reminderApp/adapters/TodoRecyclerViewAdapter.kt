package com.example.reminderApp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.R
import com.example.reminderApp.listeners.TodoItemListener
import com.example.reminderApp.models.Todo
import com.example.reminderApp.viewHolders.BaseViewHolder
import com.example.reminderApp.viewHolders.TodoViewHolder

class TodoRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder<*, TodoItemListener?>>() {
    private var todos: List<Todo> = emptyList()
    private lateinit var listener: TodoItemListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, TodoItemListener?> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_custom_recyclerview, parent, false)

        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseViewHolder<*, TodoItemListener?>, position: Int) {
        val todo = todos[position]
        (holder as BaseViewHolder<Any, TodoItemListener>).bind(todo, listener)
    }

    internal fun setTodos(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

    fun itemClicked(listener: TodoItemListener) {
        this.listener = listener
    }

    fun setFilter(queryList: List<Todo>) {
        if (!todos.isNullOrEmpty()) {
            todos = arrayListOf()
        }

        (todos as ArrayList<Todo>).addAll(queryList)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Todo {
        try {
            return todos[position]
        } catch (e: RuntimeException) {
            throw RuntimeException("Item does not exist!")
        }
    }
}