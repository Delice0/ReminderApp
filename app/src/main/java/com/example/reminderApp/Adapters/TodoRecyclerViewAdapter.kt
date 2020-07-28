package com.example.reminderApp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.Utils.DateUtil
import com.example.reminderApp.ViewHolders.TodoViewHolder
import com.example.reminderApp.ViewHolders.BaseViewHolder
import timber.log.Timber
import java.time.LocalDateTime

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
        Timber.i("$todo") // TODO FIX
        (holder as BaseViewHolder<Any, TodoItemListener>).bind(todo, listener)
    }

    internal fun setTodos(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

    fun itemClicked(listener: TodoItemListener) {
        this.listener = listener
    }

}