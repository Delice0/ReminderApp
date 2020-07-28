package com.example.reminderApp.ViewHolders

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.Utils.DateUtil

class DoneTodoViewHolder(view: View) : BaseViewHolder<Todo, TodoItemListener?>(view) {
    private var title: TextView? = null
    private var doneDate: TextView? = null

    init {
        title = itemView.findViewById(R.id.done_todo_custom_title)
        doneDate = itemView.findViewById(R.id.done_todo_custom_done_date)
    }

    override fun bind(item: Todo, listener: TodoItemListener?) {
        title?.text = item.title
        doneDate?.text = item.doneDate!!.format(DateUtil.dateTimeFormat_simple)
    }
}