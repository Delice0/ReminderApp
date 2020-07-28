package com.example.reminderApp.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.Utils.DateUtil
import com.example.reminderApp.ViewModels.TodoViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val SELECT_DATE: String = "Select date"

class AddFragment : DialogFragment() {
    private lateinit var mViewModel: TodoViewModel

    private var pickedDateTime: LocalDateTime? = null
    private var pickedPriority = 0
    private var priorities: IntArray? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        priorities = resources.getIntArray(R.array.priorities)

        // Views
        val dropdown = view.findViewById<Spinner>(R.id.addFrag_dropdown_priority)

        // Priority dropdown adapter
        if (dropdown != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                priorities!!.toList()
            )
            dropdown.adapter = adapter
        }

        initializeListeners()
    }

    private fun initializeListeners() {
        addFrag_calender.setOnClickListener { activateDateTimePicker() }
        addFrag_create_todo.setOnClickListener { activateCreateTodo() }
        addFrag_dropdown_priority.onItemSelectedListener = activatePriority()
    }

    private fun activatePriority() = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            pickedPriority = priorities!![position]
        }
    }

    private fun activateCreateTodo() {
        // Create a todoObject from users selections
        val todo = Todo(
            addFrag_title.editableText.toString(),
            addFrag_description.editableText.toString(),
            pickedPriority,
            pickedDateTime!!,
            LocalDateTime.now()
        )

        // TODO: Show error message to show user which field is not valid
        Timber.i("Validating fields...")
        if (isTitleValid() && isDescriptionValid() && isSelectedDateTimeValid()) {
            Timber.i("Inserting TODO $todo")
            mViewModel.insert(todo)

            dismiss()

            Toast.makeText(requireContext(), "Created todo!", Toast.LENGTH_LONG).show()
        }
    }

    private fun activateDateTimePicker() {
        val instance = Calendar.getInstance()
        val startYear = instance.get(Calendar.YEAR)
        val startMonth = instance.get(Calendar.MONTH)
        val startDay = instance.get(Calendar.DAY_OF_MONTH)
        val startHour = instance.get(Calendar.HOUR_OF_DAY)
        val startMinute = instance.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                pickedDateTime =
                    LocalDateTime.of(
                        year,
                        month + 1,
                        day,
                        hour,
                        minute
                    )
                addFrag_calender.text = pickedDateTime!!.format(DateUtil.dateTimeFormat)
            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun isTitleValid(): Boolean {
        if (addFrag_title.text.isEmpty()) {
            Timber.i("Title is not valid..")
            return false
        }
        return true
    }

    private fun isDescriptionValid(): Boolean {
        if (addFrag_description.text.isEmpty()) {
            Timber.i("Description is not valid..")
            return false
        }
        return true
    }

    private fun isSelectedDateTimeValid(): Boolean {
        if (addFrag_calender.text == SELECT_DATE) {
            Timber.i("Datetime is not valid..")
            return false
        }
        return true
    }
}