package com.example.reminderApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderApp.R
import com.example.reminderApp.ViewModels.TodoViewModel
import com.example.reminderApp.adapters.DoneTodoRecyclerViewAdapter
import com.example.reminderApp.listeners.OnBackPressedListener
import com.example.reminderApp.utils.AlertUtil
import timber.log.Timber

@Suppress("NAME_SHADOWING")
class DoneFragment : Fragment(), OnBackPressedListener {
    private lateinit var mViewModel: TodoViewModel
    private lateinit var mAdapter: DoneTodoRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.doneFrag_recyclerview)
        val clearButton = view.findViewById<Button>(R.id.doneFrag_clearButton)

        mAdapter = DoneTodoRecyclerViewAdapter()

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        mViewModel.allDoneTodos.observe(viewLifecycleOwner, Observer {
            Timber.i("ALL DONE TODOS $it")
            mAdapter.setDoneTodos(it)
        })

        clearButton.setOnClickListener { activateClearList() }
    }

    // TODO FIX default alert dialog colors
    fun activateClearList() {
        AlertUtil.buildAlertPopup(requireView(), AlertUtil.Titles.CONFIRMATION.title, "Clear list?")
            .setPositiveButton(AlertUtil.PositiveAnswer.YES.answer) { _, _ ->
                mViewModel.deleteAllDoneTodos()
                mAdapter.notifyDataSetChanged()

            }
            .setNegativeButton(AlertUtil.NegativeAnswer.CANCEL.answer) { dialog, _ -> dialog.dismiss() }
            .show()
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