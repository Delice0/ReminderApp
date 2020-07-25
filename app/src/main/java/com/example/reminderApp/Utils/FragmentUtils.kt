package com.example.reminderApp.Utils

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentUtils {
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }
}

