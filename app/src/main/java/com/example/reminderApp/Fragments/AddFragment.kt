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
import java.util.*

class AddFragment : DialogFragment() {
    private lateinit var mViewModel: TodoViewModel

    private val SELECT_DATE = "Select date"

    private var pickedDateTime = ""
    private var pickedPriority = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        val priorities = resources.getStringArray(R.array.priorities)

        // Views
        val dropdown = view.findViewById<Spinner>(R.id.addFrag_dropdown_priority)

        // Priority dropdown adapter
        if (dropdown != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                priorities
            )
            dropdown.adapter = adapter
        }

        addFrag_calender.setOnClickListener { getDateTimePicker() }

        addFrag_dropdown_priority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    pickedPriority = priorities[position]
                }
            }

        // Finish creating the todo listener
        addFrag_create_todo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (pickedPriority.isEmpty()) {

                    // Set default value for priority
                    pickedPriority = priorities[0]
                }

                val todo = Todo(
                    addFrag_title.editableText.toString(),
                    addFrag_description.editableText.toString(),
                    pickedPriority,
                    pickedDateTime,
                    DateUtil.dateTimeFormat(LocalDateTime.now())
                )

                Timber.i("Validating fields...")
                if (isTitleValid() && isDescriptionValid() && isSelectedDateTimeValid()) {
                    Timber.i("Inserting TODO ${todo}")
                    mViewModel.insert(todo)

                    dismiss()

                    Toast.makeText(requireContext(), "Created todo!", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun getDateTimePicker() {
        val instance = Calendar.getInstance()
        val startYear = instance.get(Calendar.YEAR)
        val startMonth = instance.get(Calendar.MONTH)
        val startDay = instance.get(Calendar.DAY_OF_MONTH)
        val startHour = instance.get(Calendar.HOUR_OF_DAY)
        val startMinute = instance.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        pickedDateTime = DateUtil.simpleDateTimeFormat(
                            LocalDateTime.of(
                                year,
                                month + 1,
                                day,
                                hour,
                                minute
                            ))
                    addFrag_calender.text = pickedDateTime
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