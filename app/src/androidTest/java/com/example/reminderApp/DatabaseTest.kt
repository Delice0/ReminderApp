package com.example.reminderApp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.reminderApp.daos.TodoDao
import com.example.reminderApp.database.TodoRoomDatabase
import com.example.reminderApp.models.Todo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime

@RunWith(AndroidJUnit4ClassRunner::class)
class DatabaseTest {
    private val testObject = Todo("Test title", "Test desc", "Test priority", LocalDateTime.now(), LocalDateTime.now())
    private lateinit var db : TodoRoomDatabase
    private lateinit var todoDao : TodoDao

    // @InstantTaskExecutor rule tells the test that everything must be synchronized
    // JvmField compiles this to java field (avoids stupid exceptions)
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Build our TodoRoomDatabase with main thread queries
        db = Room.inMemoryDatabaseBuilder(context, TodoRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        todoDao = db.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertTest() = runBlocking {
        // Insert to DB
        todoDao.insert(testObject)

        // Get from DB
        val allItems = todoDao.getAllTodos().getOrAwaitValue()

        // Assert created object equals to object from DB
        assertTrue(allItems.any { it == testObject })
    }

    @Test
    fun deleteTest() = runBlocking {
        todoDao.insert(testObject)

        val allItems = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue(allItems.any { it == testObject})

        // The test object does not autogenerate any ID thats why I apply the generated ID from Room to the object itself
        testObject.apply { id = allItems[0].id}

        todoDao.delete(testObject)

        val refreshedAllItems = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue("Todo with ID [${testObject.id}] is not deleted as there is still objects in list: ${refreshedAllItems.size}",
            refreshedAllItems.isEmpty())
    }
}