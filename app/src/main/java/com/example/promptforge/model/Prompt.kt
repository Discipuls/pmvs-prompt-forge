package com.example.promptforge.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prompts")
data class Prompt(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var raw_prompt: String = "",
    var improved_prompt: String = "",
    var rating: Int = 0,
    var timestamp: Long = 0
)
