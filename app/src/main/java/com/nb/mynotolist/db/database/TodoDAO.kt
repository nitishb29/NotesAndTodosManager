package com.nb.mynotolist.db.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nb.mynotolist.db.entities.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: TodoEntity)

    @Delete
    suspend fun deleteItem(item: TodoEntity)

    @Query("SELECT * FROM T_TODO ORDER BY id DESC")
    fun getAllItems(): Flow<List<TodoEntity>>

    @Transaction
    @Query("SELECT * FROM T_TODO WHERE description LIKE :query")
    fun searchItem(query: String?): Flow<List<TodoEntity>>
}