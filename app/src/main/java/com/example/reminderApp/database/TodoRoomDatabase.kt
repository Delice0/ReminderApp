package com.example.reminderApp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reminderApp.converters.LocalDateTimeConverter
import com.example.reminderApp.daos.TodoDao
import com.example.reminderApp.models.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter::class)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null

        fun getDatabase(context: Context): TodoRoomDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "todo_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}