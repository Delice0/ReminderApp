package com.example.reminderApp.DAOs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.reminderApp.Models.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg todo: Todo)

    @Query("SELECT * FROM todo_table WHERE is_done = 0 ORDER BY due_date ASC")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTodos()

    @Delete
    fun delete(todo: Todo)

    @Ignore
    @Query("SELECT * FROM todo_table WHERE is_done = 1")
    fun getAllDoneTodos(): LiveData<List<Todo>>

    @Update
    suspend fun finish(todo: Todo)

    @Query("DELETE FROM todo_table WHERE is_done = 1")
    fun deleteDoneTodos()
}