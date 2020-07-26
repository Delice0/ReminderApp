package com.example.reminderApp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.Adapters.DoneTodoRecyclerViewAdapter
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel
import timber.log.Timber

class DoneFragment : Fragment() {
    private lateinit var mViewModel: TodoViewModel
    private lateinit var mAdapter: DoneTodoRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.doneFrag_recyclerview)

        mAdapter = DoneTodoRecyclerViewAdapter()

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        mViewModel.allDoneTodos.observe(viewLifecycleOwner, Observer {
            Timber.i("ALL DONE TODOS $it")
            mAdapter.setDoneTodos(it)
        })
    }
}