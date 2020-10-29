package com.example.reminderApp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.reminderApp.daos.TodoDao
import com.example.reminderApp.database.TodoRoomDatabase
import com.example.reminderApp.models.Todo
import kotlinx.coroutines.runBlocking
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.io.IOException
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    // @InstantTaskExecutor rule tells the test that everything must be synchronized
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var logAllAlwaysRule: TimberTestRule? = TimberTestRule.logAllAlways()

    private lateinit var db: TodoRoomDatabase
    private lateinit var todoDao: TodoDao

    private val testObject = Todo(
        "Test title",
        "Test desc",
        "Test priority",
        LocalDateTime.now(),
        LocalDateTime.now()
    )

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
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertTest() = runBlocking {
        // Insert to DB
        insertSingleTestObjectToDB()

        // Get from DB
        val allItems = todoDao.getAllTodos().getOrAwaitValue()

        val todo = Todo("fail", "failing", "fail", LocalDateTime.now(), LocalDateTime.now())
        
        // Assert created object equals to object from DB
        assertTrue("Item has not been inserted to DB", allItems.any { it == todo }) // TODO REMEMBER TO REVERT
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

        var allTodos = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue(allTodos.any { it == testObject })

        // The manual created test object does not autogenerate any ID thats why its ID is applied to the one in the database
        updateTestObjectID(allTodos[0].id!!)

        todoDao.delete(testObject)

        allTodos = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue(
            "Todo with ID [${testObject.id}] is not deleted as there is still objects in list: ${allTodos.size}",
            allTodos.isEmpty()
        )
    }

    @Test
    fun deleteAllTest() = runBlocking {
        val todoList = arrayOf(testObject, testObject, testObject, testObject, testObject)

        todoDao.insert(*todoList)

        var allTodos = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue(
            "Not all todos as has been inserted to DB: Expected [${todoList.size}], Actual [${allTodos.size}]",
            allTodos.size == 5
        )

        todoDao.deleteAllTodos()

        allTodos = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue("There is still todos in DB", allTodos.isEmpty())
    }

    @Test
    fun deleteDoneTodosTest() = runBlocking {
        val doneTestObject = testObject.apply { isDone = true }
        val doneTestObject2 = testObject.apply { isDone = true }

        val doneTodoObjectList = arrayOf(doneTestObject, doneTestObject2)

        todoDao.insert(*doneTodoObjectList)

        val allDoneTodos = todoDao.getAllDoneTodos().getOrAwaitValue()

        assertTrue(
            "No items in DB: Expected [${doneTodoObjectList.size}, Actual [${allDoneTodos.size}",
            allDoneTodos.size == doneTodoObjectList.size
        )
    }

    @Test
    fun updateTest() = runBlocking {
        insertSingleTestObjectToDB()

        var allTodos = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue(allTodos.any { it == testObject })

        updateTestObjectID(allTodos[0].id!!)

        todoDao.update(testObject.apply { title = "Updated title" })

        allTodos = todoDao.getAllTodos().getOrAwaitValue()

        assertTrue("Title is not updated: Expected [${testObject.title}], Actual [${allTodos[0].title}]",
            allTodos.any { item -> item.title == testObject.title })
    }

    private fun insertSingleTestObjectToDB() = runBlocking {
        todoDao.insert(testObject)
    }

    private fun updateTestObjectID(generatedID: Long) {
        testObject.apply { id = generatedID }
    }
}