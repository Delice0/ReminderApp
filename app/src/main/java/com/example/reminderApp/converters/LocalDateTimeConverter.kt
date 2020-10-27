package com.example.reminderApp.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) {
            null
        } else {
            var currentDateString = dateString

            if (!dateString.contains("T")) {
                currentDateString = dateString.substring(0, 10) + "T" + dateString.substring(11)
            }
            return LocalDateTime.parse(currentDateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}