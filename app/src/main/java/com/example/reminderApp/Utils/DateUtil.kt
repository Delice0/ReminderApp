package com.example.reminderApp.Utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object {
        private val dateFormat = DateTimeFormatter.ofPattern("dd/MM-yyyy")
        private val dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM-yyyy hh:mm")
        private val dateTimeFormat_simple = DateTimeFormatter.ofPattern("d.LLL hh:mm")

        fun dateFormat(date: LocalDate) = date.format(dateFormat)

        fun dateTimeFormat(datetime: LocalDateTime) = datetime.format(dateTimeFormat)

        fun simpleDateTimeFormat(dateTime: LocalDateTime) = dateTime.format(dateTimeFormat_simple)
    }
}