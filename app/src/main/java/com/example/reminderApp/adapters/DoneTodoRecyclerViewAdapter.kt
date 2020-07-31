package com.example.reminderApp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.listeners.TodoItemListener
import com.example.reminderApp.models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.viewHolders.BaseViewHolder
import com.example.reminderApp.viewHolders.DoneTodoViewHolder

class DoneTodoRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder<*, TodoItemListener?>>() {
    private var doneTodos: List<Todo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, TodoItemListener?> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.done_todo_custom_recyclerview, parent, false)

        return DoneTodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return doneTodos.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseViewHolder<*, TodoItemListener?>, position: Int) {
        val doneTodo = doneTodos[position]
        (holder as BaseViewHolder<Any, TodoItemListener>).bind(doneTodo, null)
    }

    internal fun setDoneTodos(todo: List<Todo>) {
        this.doneTodos = todo
        notifyDataSetChanged()
    }
}