package com.example.reminderApp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel

class DoneFragment : Fragment() {
    private lateinit var mViewModel: TodoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        mViewModel.allDoneTodos.observe(viewLifecycleOwner, Observer {
            //TODO: Create custom adapter!!
        })

    }
}