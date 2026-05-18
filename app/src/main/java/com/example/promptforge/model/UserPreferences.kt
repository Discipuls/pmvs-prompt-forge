package com.example.promptforge.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey
    var id: Int = 1,
    var custom_instructions: String = ""
)
