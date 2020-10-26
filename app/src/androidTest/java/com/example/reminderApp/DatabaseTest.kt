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
import org.junit.*
import org.junit.Assert.assertTrue
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
        insertSingleTestObjectToDB()

        // Get from DB
        val allItems = todoDao.getAllTodos().getOrAwaitValue()

        // Assert created object equals to object from DB
        assertTrue("Item has not been inserted to DB", allItems.any { it == testObject })
    }

    @Test
    fun insertDoneTodos() = runBlocking {
        todoDao.insert(testObject.apply { isDone = true })

        val allItems = todoDao.getAllDoneTodos().getOrAwaitValue()

        assertTrue("Item has not been set to done", allItems.any { it.isDone })
    }

    @Test
    fun deleteTest() = runBlocking {
        insertSingleTestObjectToDB()

        var allItems = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue(allItems.any { it == testObject})

        // The manual created test object does not autogenerate any ID thats why its ID is applied to the one in the database
        updateTestObjectID(allItems[0].id!!)

        todoDao.delete(testObject)

        allItems = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue("Todo with ID [${testObject.id}] is not deleted as there is still objects in list: ${allItems.size}",
            allItems.isEmpty())
    }


    private fun insertSingleTestObjectToDB() = runBlocking{
        todoDao.insert(testObject)
    }

    private fun updateTestObjectID(generatedID: Long) {
        testObject.apply { id = generatedID }
    }
}