package com.example.reminderApp.ViewHolders

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.Utils.DateUtil

class TodoViewHolder(view: View) : BaseViewHolder<Todo, TodoItemListener?>(view) {
    private var title: TextView? = null
    private var description: TextView? = null
    private var priority: TextView? = null
    private var created: TextView? = null
    private var duedateDate: TextView? = null
    private var duedateTime: TextView? = null
    private var checkbox: CheckBox? = null

    init {
        title = itemView.findViewById(R.id.todo_custom_title)
        description = itemView.findViewById(R.id.todo_custom_description)
        priority = itemView.findViewById(R.id.todo_custom_priority)
        created = itemView.findViewById(R.id.todo_custom_created)
        duedateDate = itemView.findViewById(R.id.todo_custom_duedate_date)
        duedateTime = itemView.findViewById(R.id.todo_custom_duedate_time)
        checkbox = itemView.findViewById(R.id.todo_custom_checkbox)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(item: Todo, listener: TodoItemListener?) {
        if (item.description.length >= 23) {
            description?.text = item.description.substring(0,23) + "..."
        } else {
            description?.text = item.description
        }

        duedateDate?.text = item.dueDate.toLocalDate().format(DateUtil.dateFormat_simple)
        duedateTime?.text = item.dueDate.toLocalTime().toString()

        title?.text = item.title
        priority?.text = item.priority.toString()
        checkbox?.isChecked = item.isDone
        created?.text = item.createdDate.format(DateUtil.dateTimeFormat_simple)

        itemView.setOnClickListener { listener?.onClickItem(itemView, adapterPosition) }
        checkbox?.setOnClickListener { listener?.onClickItem(checkbox!!, adapterPosition) }
    }
}