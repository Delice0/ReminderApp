package com.example.reminderApp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.reminderApp.Converters.LocalDateTimeConverter
import com.example.reminderApp.DAOs.TodoDao
import com.example.reminderApp.Models.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter::class)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TodoRoomDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "todo_database"
                )
                    .addCallback(todoDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    private class todoDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.todoDao())
                }
            }
        }

        // Populate DB
        suspend fun populateDatabase(todoDao: TodoDao) {
            // Clear database
            todoDao.deleteAllTodos()

            // Add samples
            val todo1 = Todo(
                "Pick peter",
                "Pick peter from school",
                8,
                LocalDateTime.of(2005,10,5, 10, 50),
                LocalDateTime.of(2005,10,5, 10, 50))
            todo1.isDone = false

            val todo2 = Todo(
                "Drin cola",
                "Drink 10 liters of cola",
                4,
                LocalDateTime.of(2005,10,5, 10, 50),
                LocalDateTime.of(2005,10,5, 10, 50))
            todo2.isDone = false

            val todo3 = Todo(
                "Homework",
                "Do homework before school",
                2,
                LocalDateTime.of(2005,10,5, 10, 50),
                LocalDateTime.of(2005,10,5, 10, 50))
            todo3.isDone = false

            val todo4 = Todo(
                "TEST DONE",
                "TESTETETET",
                5,
                LocalDateTime.of(2005,10,5, 10, 50),
                LocalDateTime.of(2005,10,5, 10, 50))
            todo4.doneDate = LocalDateTime.now().plusDays(5)
            todo4.isDone = true

            val todo5 = Todo(
                "TEST DONE 2",
                "TESTETETET",
                5,
                LocalDateTime.of(2005,10,5, 10, 50),
                LocalDateTime.of(2005,10,5, 10, 50))
            todo5.doneDate = LocalDateTime.now()
            todo5.isDone = true

            todoDao.insert(todo1, todo2, todo3, todo4, todo5)
        }
    }
}