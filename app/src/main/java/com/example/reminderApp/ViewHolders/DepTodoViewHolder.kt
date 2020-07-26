package com.example.reminderApp.ViewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R

@Deprecated("This is deprecated", ReplaceWith("TodoViewHolder()"))
class DepTodoViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.todo_custom_recyclerview, parent, false)) {

    private var title: TextView? = null
    private var description: TextView? = null
    private var priority: TextView? = null
    private var created: TextView? = null
    private var duedate: TextView? = null
    private var checkbox: CheckBox? = null

    init {
        title = itemView.findViewById(R.id.todo_custom_title)
        description = itemView.findViewById(R.id.todo_custom_description)
        priority = itemView.findViewById(R.id.todo_custom_priority)
        created = itemView.findViewById(R.id.todo_custom_created)
        duedate = itemView.findViewById(R.id.todo_custom_duedate)
        checkbox = itemView.findViewById(R.id.todo_custom_checkbox)
    }

    fun bind(todo: Todo, listener: TodoItemListener) {
        title?.text = todo.title
        description?.text = todo.description
        priority?.text = todo.priority
        created?.text = todo.createdDate
        duedate?.text = todo.dueDate
        checkbox?.isChecked = todo.isDone

        itemView.setOnClickListener { listener.onClickItem(itemView, adapterPosition) }
        checkbox?.setOnClickListener { listener.onClickItem(checkbox!!, adapterPosition) }
    }

}