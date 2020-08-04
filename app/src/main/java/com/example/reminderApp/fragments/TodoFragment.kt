package com.example.reminderApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel
import com.example.reminderApp.adapters.TodoRecyclerViewAdapter
import com.example.reminderApp.listeners.OnBackPressedListener
import com.example.reminderApp.listeners.TodoItemListener
import com.example.reminderApp.shortToast
import com.example.reminderApp.utils.AlertUtil
import kotlinx.android.synthetic.main.todo_custom_recyclerview.view.*
import timber.log.Timber

@Suppress("NAME_SHADOWING")
class TodoFragment : Fragment(), OnBackPressedListener {
    private lateinit var mViewModel: TodoViewModel
    private lateinit var mAdapter: TodoRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
                recyclerView.findViewHolderForAdapterPosition(i)?.itemView -> {
                    val todoDetailFragment = TodoDetailFragment()

                    childFragmentManager.beginTransaction()
                        .add(R.id.todoFrag_container, todoDetailFragment, todoDetailFragment.javaClass.simpleName)
                        .addToBackStack("childBsTodo")
                        .commit()

                    val todo = mViewModel.allTodos.value?.get(i)
                    Timber.i("Picked todo $todo................")

                    mViewModel.select(todo!!)
                }
            }
        })

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        mViewModel.allTodos.observe(viewLifecycleOwner, Observer {

            // Update the cached copy of the todos in the adapter.
            it.let { mAdapter.setTodos(it) }
        })

        // Initialize ItemTouchHelper AKA in this scenario: swipe function
        val itemtouchHelper = ItemTouchHelper(activateItemTouchSwipe())
        itemtouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun activateCheckBox(ch: CheckBox, i: Int) {
        if (ch.isChecked) {
            AlertUtil.buildAlertPopup(requireView(), AlertUtil.Titles.CONFIRMATION.title, "Done?")
                .setPositiveButton("YES") { _, _ ->
                    Timber.i("Updating todo: ${mViewModel.allTodos.value?.get(i)?.title}")
                    shortToast("Updating todo: ${mViewModel.allTodos.value?.get(i)?.title}")

                    mViewModel.finish(i)
                    mAdapter.notifyDataSetChanged() }
                .setNegativeButton("CANCEL") { _, _ ->
                    Timber.i("Updating todo is cancelled..")
                    shortToast("Cancelled..")

                    ch.isChecked = false }
                .show()
        }
    }

    private fun activateItemTouchSwipe() =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Timber.i("Confirming deleting todo..")

                AlertUtil.buildAlertPopup(view!!, AlertUtil.Titles.CONFIRMATION.title,
                    "Are you sure that you wanna delete this todo?")

                    .setPositiveButton("DELETE") { _, _ ->
                        Timber.i("Deleting todo: ${mViewModel.allTodos.value?.get(viewHolder.adapterPosition)}")
                        mViewModel.delete(viewHolder.adapterPosition)

                        shortToast("Deleted following todo succesfully!") }
                    .setNegativeButton("CANCEL") { _, _ ->
                        Timber.i("User has cancelled deleting todo..")

                        mAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        shortToast("Cancelled..") }
                    .show()
            }
        }

    override fun onBackPressed(): Boolean {
        Timber.i("Validating back button press..")
        if (parentFragmentManager.backStackEntryCount == 0) {
            if (childFragmentManager.backStackEntryCount > 0) {
                val transaction = childFragmentManager.beginTransaction()

                val childFragments = childFragmentManager.fragments

                if (childFragments.size > 0) {
                    Timber.i("Removing any ChildFragment is now being processed..")
                    for (childFragment in childFragments) {
                        Timber.i("${childFragment.tag} is being removed..")
                        transaction.remove(childFragment)
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
