package com.example.reminderApp.utils

import java.time.format.DateTimeFormatter

class DateUtil {
    companion object {
        val dateFormat_simple = DateTimeFormatter.ofPattern("d.LLL")
        val dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM-yyyy HH:mm")
        val dateTimeFormat_simple = DateTimeFormatter.ofPattern("d.LLL HH:mm")
    }
}