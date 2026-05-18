package com.example.promptforge.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.promptforge.model.Prompt
import kotlinx.coroutines.flow.Flow

@Dao
interface PromptDao {
    @Query("SELECT * FROM prompts ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Prompt>>

    @Query("SELECT * FROM prompts WHERE id = :id")
    suspend fun getById(id: Int): Prompt?

    @Insert
    suspend fun insert(p: Prompt): Long

    @Delete
    suspend fun delete(p: Prompt)
}
