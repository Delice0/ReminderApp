package com.example.reminderApp.viewHolders

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.reminderApp.R
import com.example.reminderApp.listeners.TodoItemListener
import com.example.reminderApp.models.Todo
import com.example.reminderApp.utils.DateUtil
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TodoViewHolder(view: View) : BaseViewHolder<Todo, TodoItemListener?>(view) {
    // Get current background as a drawable
    private val currBackgroundGradientDrawable = view.background as GradientDrawable

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

    override fun bind(item: Todo, listener: TodoItemListener?) {
        limitTitleInputIfNecessary(item)
        limitDescriptionIfNecessary(item)

        addColorStrokeToDueDateDependingOnDate(item)

        duedateDate?.text = item.dueDate.toLocalDate().format(DateUtil.dateFormat_simple)
        duedateTime?.text = item.dueDate.toLocalTime().toString()

        priority?.text = item.priority.toString()
        checkbox?.isChecked = item.isDone
        created?.text = item.createdDate.format(DateUtil.dateTimeFormat_simple)

        itemView.setOnClickListener { listener?.onClickItem(itemView, adapterPosition) }
        checkbox?.setOnClickListener { listener?.onClickItem(checkbox!!, adapterPosition) }
    }

    private fun addColorStrokeToDueDateDependingOnDate(item: Todo) {
        val gd = GradientDrawable()

        when {
            // If due date is today
            item.dueDate.toLocalDate() == LocalDate.now() -> {
                // Set border color
                gd.setStroke(3, Color.RED)
                gd.cornerRadius = currBackgroundGradientDrawable.cornerRadius
                gd.color = currBackgroundGradientDrawable.color

                // Set border color for checkbox
                checkbox?.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.colorDanger))
                itemView.background = gd
            }

            // If due date is between tomorrow and 3 days from tomorrow
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
            else -> { itemView.background = currBackgroundGradientDrawable }
        }
    }

    private fun limitTitleInputIfNecessary(item: Todo) {
        if (item.title.length >= 16) {
            title?.text = item.title.substring(0, 16) + ".."
        } else {
            title?.text = item.title
        }
    }

    private fun limitDescriptionIfNecessary(item: Todo) {
        if (item.description.length >= 23) {
            description?.text = item.description.substring(0, 23) + ".."
        } else {
            description?.text = item.description
        }
    }
}