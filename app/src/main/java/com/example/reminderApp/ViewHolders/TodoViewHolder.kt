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
    private var duedate_date: TextView? = null
    private var duedate_time: TextView? = null
    private var checkbox: CheckBox? = null

    val splitDateAndTimeRegex = "(\\d+\\.\\w{3})\\s(\\d+:\\d+)".toRegex()

    init {
        title = itemView.findViewById(R.id.todo_custom_title)
        description = itemView.findViewById(R.id.todo_custom_description)
        priority = itemView.findViewById(R.id.todo_custom_priority)
        created = itemView.findViewById(R.id.todo_custom_created)
        duedate_date = itemView.findViewById(R.id.todo_custom_duedate_date)
        duedate_time = itemView.findViewById(R.id.todo_custom_duedate_time)
        checkbox = itemView.findViewById(R.id.todo_custom_checkbox)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(item: Todo, listener: TodoItemListener?) {
        val i = item.dueDate.format(DateUtil.dateTimeFormat_simple)

        var onlyDate = ""
        var onlyTime = ""

        if (splitDateAndTimeRegex.matches(i)) {
            onlyDate = splitDateAndTimeRegex.matchEntire(i)!!.groupValues[1]
            onlyTime = splitDateAndTimeRegex.matchEntire(i)!!.groupValues[2]
        }

        if (item.description.length >= 23) {
            description?.text = item.description.substring(0,23) + "..."
        } else {
            description?.text = item.description
        }

        duedate_date?.text = onlyDate
        duedate_time?.text = onlyTime

        title?.text = item.title
        priority?.text = item.priority.toString()
        checkbox?.isChecked = item.isDone
        created?.text = item.createdDate.format(DateUtil.dateTimeFormat_simple)

        itemView.setOnClickListener { listener?.onClickItem(itemView, adapterPosition) }
        checkbox?.setOnClickListener { listener?.onClickItem(checkbox!!, adapterPosition) }
    }
}