package com.example.reminderApp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel
import com.example.reminderApp.models.Todo
import com.example.reminderApp.utils.AlertUtil
import com.example.reminderApp.utils.DateUtil
import com.example.reminderApp.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_add.*
import timber.log.Timber
import java.time.LocalDateTime
import java.util.*

private const val SELECT_DATE: String = "Select date"

class AddFragment : DialogFragment() {
    private lateinit var mViewModel: TodoViewModel

    private var pickedDateTime: LocalDateTime? = null
    private var pickedPriority = 0
    private var priorities: IntArray? = null
    private var title: TextView? = null
    private var description: TextView? = null
    private var dropdown: AutoCompleteTextView? = null
    private var selectDateBtn: Button? = null
    private var finishBtn: Button? = null
    private var cancelBtn: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set round border
        dialog?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.layout_round_boarder, null));

        initializeViews()

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        priorities = resources.getIntArray(R.array.priorities)

        if (dropdown != null) {
            val adapter = ArrayAdapter<Int?>(
                view.context,
                R.layout.dropdown_menu_popup_item,
                priorities!!.toList()
            )

            dropdown?.setAdapter(adapter)
        }

        initializeListeners()
    }

    private fun initializeViews() {
        title = view?.findViewById(R.id.addfrag_title)
        description = view?.findViewById(R.id.addFrag_description)
        dropdown = view?.findViewById(R.id.addFrag_dropdown_priority)
        finishBtn = view?.findViewById(R.id.addFrag_button_create_todo)
        cancelBtn = view?.findViewById(R.id.addFrag_button_cancel)
        selectDateBtn = view?.findViewById(R.id.addFrag_button_select_date)
    }

    private fun initializeListeners() {
        // Select date button listeners
        selectDateBtn?.setOnClickListener { activateDateTimePicker() }
        selectDateBtn?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                selectDateBtn?.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nothing
            }
        })

        // Select priority listeners
        dropdown?.setOnItemClickListener { parent, view, position, id -> setSelectedPriority(position) }
        dropdown?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                addFrag_layout_outlinedTextField_priority.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

        })

        // Cancel creating a todo listener
        cancelBtn?.setOnClickListener {
            if (description != null || description?.text!!.isNotEmpty() || title != null || title?.text!!.isNotEmpty()) {
                val builder = AlertUtil.buildAlertPopup(
                    requireView(),
                    AlertUtil.Titles.CONFIRMATION.title,
                    "Your changes will be lost. Proceed?")

                val create = builder.create()

                builder
                    .setPositiveButton(AlertUtil.PositiveAnswer.YES.answer) { dialog, which ->
                        // Dismiss this fragment
                        dismiss()
                    }
                    .setNegativeButton(AlertUtil.NegativeAnswer.NO.answer) {dialog, which ->
                        create.cancel()
                    }.show()

            } else dismiss()
        }

        // Create todo listener
        finishBtn?.setOnClickListener {
            Timber.i("Validating fields...")
            if (isTitleValid() && isDescriptionValid() && isSelectedDateTimeValid() && isPrioritySet()) {
                activateCreateTodo()

                dismiss()

                ToastUtil.shortToast(requireContext(), "Created todo!")
            } else {
                ToastUtil.shortToast(requireContext(), "Remember to fill out fields!")
            }
        }
    }

    private fun setSelectedPriority(position: Int) {
        pickedPriority = priorities!![position]
    }

    private fun activateCreateTodo() {
        // Create a todoObject from users selections
        val todo = Todo(
            title?.text.toString(),
            description?.editableText.toString(),
            pickedPriority,
            pickedDateTime!!,
            LocalDateTime.now()
        )

        Timber.i("Inserting TODO $todo")
        mViewModel.insert(todo)
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
                addFrag_button_select_date.text = pickedDateTime!!.format(DateUtil.dateTimeFormat)
            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun isTitleValid(): Boolean {
        if (title?.text!!.isEmpty()) {
            Timber.i("Title is not valid..")
            title?.error = "Title cannot be empty.."
            return false
        }
        return true
    }

    private fun isDescriptionValid(): Boolean {
        if (description?.text!!.isEmpty()) {
            Timber.i("Description is not validated..")
            title?.error = "Description cannot be empty.."
            return false
        }
        return true
    }

    private fun isSelectedDateTimeValid(): Boolean {
        if (addFrag_button_select_date.text == SELECT_DATE) {
            Timber.i("Datetime is not valid..")
            addFrag_button_select_date.error = "You must choose a priority.."
            return false
        }
        return true
    }

    private fun isPrioritySet(): Boolean {
        if (pickedPriority == 0) {
            Timber.i("Priority is not selected..")
            addFrag_layout_outlinedTextField_priority.error = "You must choose a priority.."
            return false
        }
        return true
    }
}