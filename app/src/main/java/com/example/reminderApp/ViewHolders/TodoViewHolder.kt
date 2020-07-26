package com.example.reminderApp.ViewHolders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R

class TodoViewHolder(view: View) : BaseViewHolder<Todo, TodoItemListener?>(view) {
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

    override fun bind(item: Todo, listener: TodoItemListener?) {
        title?.text = item.title
        description?.text = item.description
        priority?.text = item.priority
        created?.text = item.createdDate
        duedate?.text = item.dueDate
        checkbox?.isChecked = item.isDone

        itemView.setOnClickListener { listener?.onClickItem(itemView, adapterPosition) }
        checkbox?.setOnClickListener { listener?.onClickItem(checkbox!!, adapterPosition) }
    }
}