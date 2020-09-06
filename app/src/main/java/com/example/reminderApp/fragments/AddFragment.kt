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
import com.example.reminderApp.listeners.OnBackPressedListener
import com.example.reminderApp.models.Todo
import com.example.reminderApp.shortToast
import com.example.reminderApp.utils.AlertUtil
import com.example.reminderApp.utils.DateUtil
import kotlinx.android.synthetic.main.fragment_add.*
import timber.log.Timber
import java.time.LocalDateTime
import java.util.*

private const val SELECT_DATE: String = "Select date"

class AddFragment : DialogFragment(), OnBackPressedListener {
    private lateinit var mViewModel: TodoViewModel

    // Normal variables
    private lateinit var pickedDateTime: LocalDateTime
    private var pickedPriority = ""
    private var priorities = emptyArray<String>()

    // Views
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var dropdown: AutoCompleteTextView
    private lateinit var selectDateBtn: Button
    private lateinit var finishBtn: Button
    private lateinit var cancelBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set round border
        dialog?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.layout_round_boarder, null))

        initializeViews()

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        priorities = resources.getStringArray(R.array.priorities)

        val adapter = ArrayAdapter<String?>(
            view.context,
            R.layout.dropdown_menu_popup_item,
            priorities)

        dropdown.setAdapter(adapter)

        initializeListeners()
    }

    /**
     * Represents all views this Fragment will show, and initializing them here
     */
    private fun initializeViews() {
        title = requireView().findViewById(R.id.addfrag_title)
        description = requireView().findViewById(R.id.addFrag_description)
        dropdown = requireView().findViewById(R.id.addFrag_dropdown_priority)
        finishBtn = requireView().findViewById(R.id.addFrag_button_create_todo)
        cancelBtn = requireView().findViewById(R.id.addFrag_button_cancel)
        selectDateBtn = requireView().findViewById(R.id.addFrag_button_select_date)
    }

    /**
     * All listeners that interacts with the views in this Fragment
     * @sample selectDateBtn        Listens to events when user registering a new date and time for todoo
     * @sample dropdown             Listens to events when user choosing a priority from the dropdown-menu
     * @sample cancelBtn            Listens to events when user cancelling new todoo
     * @sample finishBtn            Listens to events when user is done adding parameters for a new todoo and clicks on finish
     */
    private fun initializeListeners() {
        selectDateBtn.setOnClickListener { activateDateTimePicker() }
        selectDateBtn.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                selectDateBtn.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        dropdown.setOnItemClickListener { _, _, position, _ -> setSelectedPriority(position) }
        dropdown.addTextChangedListener(object : TextWatcher {
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
        cancelBtn.setOnClickListener {
            if (description.text.isNotEmpty() && title.text.isNotEmpty()) {
                val builder = AlertUtil.buildAlertPopup(
                    requireView(),
                    AlertUtil.Titles.CONFIRMATION.title,
                    "Your changes will be lost. Proceed?"
                )

                val create = builder.create()

                builder
                    .setPositiveButton(AlertUtil.PositiveAnswer.YES.answer) { _, _ ->
                        dismiss()
                    }.setNegativeButton(AlertUtil.NegativeAnswer.NO.answer) { _, _ ->
                        create.cancel()
                    }.show()
            } else {
                dismiss()
            }
        }

        finishBtn.setOnClickListener {
            Timber.i("Validating fields...")

            if (isTitleValid() && isDescriptionValid() && isSelectedDateTimeValid() && isPrioritySet()) {
                activateCreateTodo()

                dismiss()

                shortToast("Created todo!")
            } else {
                shortToast("Remember to fill out fields!")
            }
        }
    }

    private fun setSelectedPriority(position: Int) {
        pickedPriority = priorities[position]
    }

    private fun activateCreateTodo() {
        // Create a todoObject from users selections
        val todo = Todo(
            title.text.toString(),
            description.editableText.toString(),
            pickedPriority,
            pickedDateTime,
            LocalDateTime.now()
        )

        Timber.i("Inserting $todo")

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
                addFrag_button_select_date.text = pickedDateTime.format(DateUtil.dateTimeFormat)
            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun isTitleValid(): Boolean {
        if (title.text.isEmpty()) {
            Timber.i("Title is not valid..")

            title.error = "Title cannot be empty.."
            return false
        }
        return true
    }

    private fun isDescriptionValid(): Boolean {
        if (description.text.isEmpty()) {
            Timber.i("Description is not valid..")

            title.error = "Description cannot be empty.."
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
        if (pickedPriority.isEmpty()) {
            Timber.i("Priority is not selected..")

            addFrag_layout_outlinedTextField_priority.error = "You must choose a priority.."
            return false
        }
        return true
    }

    override fun onBackPressed(): Boolean {
        Timber.i("Validating back button press..")

        if (parentFragmentManager.backStackEntryCount == 0) {
            if (childFragmentManager.backStackEntryCount > 0) {
                val transaction = childFragmentManager.beginTransaction()

                val childFragments = childFragmentManager.fragments

                if (childFragments.size > 0) {
                    Timber.i("Removing any ChildFragment is now being processed..")
                    for (childFrag in childFragments) {
                        Timber.i("${childFrag.tag} is being removed..")
                        transaction.remove(childFrag)
                    }

                    transaction.commit()

                    return true
                }
                return false
            }
        }
        return false
    }
}