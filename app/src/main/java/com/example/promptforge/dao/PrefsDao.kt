package com.example.promptforge.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.promptforge.model.UserPreferences

@Dao
interface PrefsDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    suspend fun get(): UserPreferences?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(prefs: UserPreferences)
}
