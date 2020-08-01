package com.example.reminderApp

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import timber.log.Timber

/**
 * Used to extend Toast function inside fragments
 * Calls @Context.shortToast
 */
fun Fragment.shortToast(message: String) = requireActivity().shortToast(message)

/**
 * Used to extend Toast function
 */
fun Context.shortToast(message: String) = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()