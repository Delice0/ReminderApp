package com.example.reminderApp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.ViewHolders.BaseViewHolder
import com.example.reminderApp.ViewHolders.DoneTodoViewHolder

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