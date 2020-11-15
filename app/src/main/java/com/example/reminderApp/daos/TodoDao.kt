package com.example.reminderApp.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reminderApp.models.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg todo: Todo)

    @Query("SELECT * FROM todo_table WHERE is_done = 0 ORDER BY CASE priority WHEN 'low' THEN 3 WHEN 'medium' THEN 2 WHEN 'high' THEN 1 END, due_date ASC")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTodos()

    @Delete
    fun delete(todo: Todo)

    @Query("SELECT * FROM todo_table WHERE is_done = 1")
    fun getAllDoneTodos(): LiveData<List<Todo>>

    @Query("UPDATE todo_table SET is_done = 1, done_date = CURRENT_TIMESTAMP WHERE todoId = :id")
    suspend fun finish(id: Long)

    @Update
    suspend fun update(todo: Todo)

    @Query("DELETE FROM todo_table WHERE is_done = 1")
    fun deleteDoneTodos()
}