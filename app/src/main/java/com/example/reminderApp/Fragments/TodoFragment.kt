package com.example.reminderApp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.Adapters.TodoRecyclerViewAdapter
import com.example.reminderApp.Listeners.TodoItemListener
import com.example.reminderApp.Models.Todo
import com.example.reminderApp.R
import com.example.reminderApp.Utils.AlertUtil
import com.example.reminderApp.Utils.ToastUtil
import com.example.reminderApp.ViewModels.TodoViewModel
import kotlinx.android.synthetic.main.todo_custom_recyclerview.view.*
import timber.log.Timber

@Suppress("NAME_SHADOWING")
class TodoFragment : Fragment() {
    private lateinit var mViewModel: TodoViewModel
    private lateinit var mAdapter: TodoRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.todoFrag_recyclerview)

        mAdapter = TodoRecyclerViewAdapter()

        mAdapter.itemClicked(TodoItemListener { view, i ->
            when (view) {
                recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.todo_custom_checkbox -> {
                    activateCheckBox(view as CheckBox, i) }
                is LinearLayout -> Timber.i("$view $i")
            }
        })

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        mViewModel.allTodos.observe(viewLifecycleOwner, Observer { todos ->
            // Update the cached copy of the words in the adapter.
            todos?.let { mAdapter.setTodos(todos) }

            todos.forEach { t: Todo? -> println("ALL TODOS $t") }
        })

        // Swipe functionality
        val itemtouchHelper = ItemTouchHelper(activateItemTouchSwipe())
        itemtouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun activateCheckBox(ch: CheckBox, i: Int) {
        if (ch.isChecked) {
            AlertUtil.buildAlertPopup(view!!, AlertUtil.Titles.CONFIRMATION.title, "Done?")
                .setPositiveButton("YES") { dialog, which ->
                    ToastUtil.shortToast(requireContext(), "Updating...${i}")

                    mViewModel.finish(i)
                    mAdapter.notifyDataSetChanged() }
                .setNegativeButton("CANCEL") { dialog, which ->
                    ToastUtil.shortToast(requireContext(), "Cancelled..")

                    ch.isChecked = false }
                .show()
        }
    }

    private fun activateItemTouchSwipe() =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertUtil.buildAlertPopup(view!!, AlertUtil.Titles.CONFIRMATION.title,
                    "Are you sure that you wanna delete this todo?")
                    .setPositiveButton("DELETE") { dialog, which ->
                        mViewModel.delete(viewHolder.adapterPosition)

                        Toast.makeText(requireContext(), "Deleted following todo succesfully!",
                        Toast.LENGTH_LONG).show() }
                    .setNegativeButton("CANCEL") { dialog, which ->
                        mAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        ToastUtil.shortToast(requireContext(), "Cancelled..") }
                    .show()
            }
        }
}
