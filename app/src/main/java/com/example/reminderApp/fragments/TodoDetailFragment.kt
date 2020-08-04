package com.example.reminderApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel
import com.example.reminderApp.models.Todo

class TodoDetailFragment: Fragment() {
    private lateinit var mViewModel: TodoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_todo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(requireParentFragment()).get(TodoViewModel::class.java)

        val title = view.findViewById<EditText>(R.id.todoDetailFrag_title)
        val description = view.findViewById<EditText>(R.id.todoDetailFrag_description)

        mViewModel.selected.observe(viewLifecycleOwner, Observer<Todo> { todo ->
            title.setText(todo.title)
            description.setText(todo.description)
        })
    }
}