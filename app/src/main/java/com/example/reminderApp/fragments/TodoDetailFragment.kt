package com.example.reminderApp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel
import com.example.reminderApp.shortToast

class TodoDetailFragment : Fragment() {
    private lateinit var mViewModel: TodoViewModel

    // Views
    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnSaveChanges: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()

        mViewModel = ViewModelProvider(requireParentFragment()).get(TodoViewModel::class.java)

        mViewModel.selected.observe(viewLifecycleOwner, { todo ->
            edtTitle.setText(todo.title)
            edtDescription.setText(todo.description)
        })

        initializeListeners()
    }

    /**
     * Disable searchview for this child fragment
     */
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val searchViewItem = menu.findItem(R.id.menu_search)

        if (searchViewItem.isVisible) {
            searchViewItem.isVisible = false
        }

        if (searchViewItem.isActionViewExpanded) {
            searchViewItem.collapseActionView()
        }
    }


    /**
     * Initialize all views this Fragment will show in the UI
     */
    private fun initializeViews() {
        edtTitle = requireView().findViewById(R.id.todoDetailFrag_title)
        edtDescription = requireView().findViewById(R.id.todoDetailFrag_description)
        btnSaveChanges = requireView().findViewById(R.id.todoDetailFrag_button_save)
    }

    /**
     * @sample btnSaveChanges           Listens to events to validate if user has changed title or descriptions
     *                                  and either updates the corresponding todoo with the new updates or leave it
     * @sample edtTitle                 Listens to events if user has changed title
     * @sample edtDescription           Listens to events if user has changed description
     */
    private fun initializeListeners() {
        var edtTitleChanged = ""
        var edtDescriptionChanged = ""

        btnSaveChanges.setOnClickListener {
            mViewModel.selected.value!!.let {
                // @edtTitleChanged and @edtDescriptionChanged will always have the same values of the original todoo at runtime
                // until user has changed them
                if (it.title != edtTitleChanged || it.description != edtDescriptionChanged) {

                    if (it.title != edtTitleChanged) {
                        it.title = edtTitleChanged
                    }

                    if (it.description != edtDescriptionChanged) {
                        it.description = edtDescriptionChanged
                    }

                    mViewModel.update(it)

                    shortToast("Updated successfully!")

                    // Return to parent fragment
                    parentFragmentManager.beginTransaction()
                        .remove(this)
                        .commit()
                } else {
                    shortToast("No changes has been made..")
                }
            }
        }

        edtTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtTitleChanged = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })


        edtDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtDescriptionChanged = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
    }
}