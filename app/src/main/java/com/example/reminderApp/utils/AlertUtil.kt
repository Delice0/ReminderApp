package com.example.reminderApp.utils

import android.app.AlertDialog
import android.view.View

class AlertUtil {
    companion object {
        fun buildAlertPopup(view: View, title: String, message: String): AlertDialog.Builder {
            return AlertDialog.Builder(view.context)
                .setTitle(title)
                .setMessage(message)
        }
    }

    enum class Titles(val title: String) {
        CONFIRMATION("Confirmation")
    }

    enum class PositiveAnswer(val answer: String) {
        YES("YES")
    }

    enum class NegativeAnswer(val answer: String) {
        CANCEL("CANCEL")
    }
}