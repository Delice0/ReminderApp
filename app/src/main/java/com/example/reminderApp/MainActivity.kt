package com.example.reminderApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reminderApp.Fragments.AddFragment
import com.example.reminderApp.Fragments.DoneFragment
import com.example.reminderApp.Fragments.TodoFragment
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
     * Bottomnavigation bar listener that changes to user-desired fragment on click event
     * */
    private fun mNavigationClickListener() =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuiItem_bottomnavigation_todo -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            todoFragment,
                            todoFragment.javaClass.simpleName
                        )
                        .addToBackStack(null)
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }

                R.id.menuItem_bottomnavigation_add -> {
                    val addFragment = AddFragment()

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            addFragment,
                            addFragment.javaClass.simpleName
                        )
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }

                R.id.menuItem_bottomnavigation_done -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            doneFragment,
                            doneFragment.javaClass.simpleName
                        )
                        .addToBackStack(null)
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}