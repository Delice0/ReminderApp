package com.example.reminderApp

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reminderApp.fragments.AddFragment
import com.example.reminderApp.fragments.DoneFragment
import com.example.reminderApp.fragments.TodoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainActivity : AppCompatActivity() {
    private val todoFragment = TodoFragment()
    private val doneFragment = DoneFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        // Initialize Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(Timber.asTree())
        }

        // Set which fragment to start application
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, todoFragment, todoFragment.javaClass.simpleName)
            .commit()

        bottomnavigation_bar.setOnNavigationItemSelectedListener(mNavigationClickListener())
    }

    /**
     * Navigation click event on BottomNavigationBar
     * */
    private fun mNavigationClickListener() =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuiItem_bottomnavigation_todo -> {
                    removeAnyChildFragmentIfPresent()

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            todoFragment,
                            todoFragment.javaClass.simpleName
                        )
                        .addToBackStack("bsTodo")
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }

                R.id.menuItem_bottomnavigation_add -> {
                    val addFragment = AddFragment()

                    addFragment.show(supportFragmentManager, "addfrag")

                    return@OnNavigationItemSelectedListener true
                }

                R.id.menuItem_bottomnavigation_done -> {
                    removeAnyChildFragmentIfPresent()

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            doneFragment,
                            doneFragment.javaClass.simpleName
                        )
                        .addToBackStack("bsDone")
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun removeAnyChildFragmentIfPresent() {
        if (isAnyChildFragmentPresent()) {
            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                val childFragmentManager = fragment.childFragmentManager

                if (childFragmentManager.backStackEntryCount > 0) {
                    val transaction = childFragmentManager.beginTransaction()
                    val childfragments = childFragmentManager.fragments

                    if (childfragments.size > 0) {
                        for (childFrag in childfragments) {
                            transaction.remove(childFrag)
                        }

                        if (childFragmentManager.backStackEntryCount > 0) {
                            Timber.i("Removing any backstack entries for the children..")
                            childFragmentManager.popBackStack()
                        }

                        transaction.commit()
                    }
                }
            }
        }
    }

    private fun isAnyChildFragmentPresent(): Boolean {
        val fragments = supportFragmentManager.fragments

        if (fragments.size > 0) {
            Timber.i("Fragments children are now being validated..")
            for (fragment in fragments) {
                val childFragmentManager = fragment.childFragmentManager

                val childFragmentCount = childFragmentManager.fragments.size

                if (childFragmentCount > 0) {
                    Timber.i("Following ChildFragments are present: ${childFragmentManager.fragments} ")
                    return true
                }
            }
            Timber.i("No parent has children..")
            return false
        }
        Timber.i("No parent is present..")
        return false
    }

    /**
     * Handle back-button pressed for childfragments
     */
    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments

        var handled = false
        for (fragment in fragmentList) {
            if (fragment is TodoFragment) {
                handled = fragment.onBackPressed()
                if (handled) {
                    break
                }
            }
        }

        if (!handled) {
            super.onBackPressed()
        }
    }
}