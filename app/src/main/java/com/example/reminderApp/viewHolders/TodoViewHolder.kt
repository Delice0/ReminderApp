package com.example.reminderApp.viewHolders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.reminderApp.listeners.TodoItemListener
import com.example.reminderApp.models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.utils.DateUtil
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TodoViewHolder(view: View) : BaseViewHolder<Todo, TodoItemListener?>(view) {
    @SuppressLint("ResourceType")
    private val DEFAULT_BORDER_LAYOUT = view.background

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

        val currBackgroundGradientDrawable = DEFAULT_BORDER_LAYOUT as GradientDrawable
        val gd = GradientDrawable()

        when {
            item.dueDate.toLocalDate() == LocalDate.now() -> {
                gd.setStroke(3, Color.RED)
                gd.cornerRadius = currBackgroundGradientDrawable.cornerRadius
                gd.color = currBackgroundGradientDrawable.color

                checkbox?.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.colorDanger))
                itemView.background = gd
            }
            ChronoUnit.DAYS.between(LocalDate.now(), item.dueDate.toLocalDate()) in 1..4 -> {
                // Border color
                gd.setStroke(3, ContextCompat.getColor(itemView.context, R.color.colorWarning))

                //Border radius
                gd.cornerRadius = currBackgroundGradientDrawable.cornerRadius

                // Background color
                gd.color = currBackgroundGradientDrawable.color

                checkbox?.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.colorWarning))

                itemView.background = gd
            }
            else -> {
                itemView.background = currBackgroundGradientDrawable
            }
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