package com.example.reminderApp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel
import com.example.reminderApp.models.Todo
import com.example.reminderApp.shortToast
import timber.log.Timber

class TodoDetailFragment: Fragment() {
    private lateinit var mViewModel: TodoViewModel

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnSaveChanges: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_todo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtTitle = view.findViewById(R.id.todoDetailFrag_title)
        edtDescription = view.findViewById(R.id.todoDetailFrag_description)
        btnSaveChanges = view.findViewById(R.id.todoDetailFrag_button_save)

        mViewModel = ViewModelProvider(requireParentFragment()).get(TodoViewModel::class.java)

        mViewModel.selected.observe(viewLifecycleOwner, Observer<Todo> { todo ->
            edtTitle.setText(todo.title)
            edtDescription.setText(todo.description)
        })

        var edtTitleChanged = ""

        edtTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtTitleChanged = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nothing
            }
        })

        var edtDescriptionChanged = ""

        edtDescription.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                edtDescriptionChanged = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nothing
            }
        })

        btnSaveChanges.setOnClickListener {
            mViewModel.selected.value!!.let {
                if (it.title != edtTitleChanged || it.description != edtDescriptionChanged) {

                    if (it.title != edtTitleChanged) {
                        Timber.i("Updating title [${it.title}] to [$edtTitleChanged]")
                        it.title = edtTitleChanged
                    }

                    if (it.description != edtDescriptionChanged) {
                        Timber.i("Updating description [${it.title}] to [$edtDescriptionChanged]")
                        it.description = edtDescriptionChanged
                    }

                    mViewModel.update(it.id)

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
    }
}