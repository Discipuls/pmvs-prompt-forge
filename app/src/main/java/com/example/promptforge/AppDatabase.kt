package com.example.promptforge

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.promptforge.dao.PrefsDao
import com.example.promptforge.dao.PromptDao
import com.example.promptforge.model.Prompt
import com.example.promptforge.model.UserPreferences

@Database(entities = [Prompt::class, UserPreferences::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun promptDao(): PromptDao
    abstract fun prefsDao(): PrefsDao

    companion object {
        var instance: AppDatabase? = null

        fun get(ctx: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "promptforge.db").build()
            }
            return instance!!
        }
    }
}
