package com.example.reminderApp.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object {
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM-yyyy")
        val dateFormat_simple = DateTimeFormatter.ofPattern("d.LLL")
        val dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM-yyyy hh:mm")
        val dateTimeFormat_simple = DateTimeFormatter.ofPattern("d.LLL hh:mm")

        fun dateFormat(date: LocalDate) = date.format(dateFormat)

        fun dateTimeFormat(datetime: LocalDateTime) = datetime.format(dateTimeFormat)

        fun simpleDateTimeFormat(dateTime: LocalDateTime) = dateTime.format(dateTimeFormat_simple)
    }
}