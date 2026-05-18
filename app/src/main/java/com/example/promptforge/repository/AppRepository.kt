package com.example.promptforge.repository

import android.content.Context
import com.example.promptforge.AppDatabase
import com.example.promptforge.api.OpenRouterApi
import com.example.promptforge.api.QuestionItem
import com.example.promptforge.model.Prompt
import com.example.promptforge.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepository(ctx: Context) {
    val db = AppDatabase.get(ctx)
    val api = OpenRouterApi()

    fun getPrompts(): Flow<List<Prompt>> = db.promptDao().getAll()

    suspend fun getPrompt(id: Int): Prompt? = db.promptDao().getById(id)

    suspend fun deletePrompt(p: Prompt) = db.promptDao().delete(p)

    suspend fun savePrompt(p: Prompt): Long = db.promptDao().insert(p)

    suspend fun getInstructions(): String {
        val prefs = db.prefsDao().get()
        return prefs?.custom_instructions ?: ""
    }

    suspend fun getQuestions(raw: String): List<QuestionItem> {
        return withContext(Dispatchers.IO) {
            val instr = getInstructions()
            api.getQuestions(raw, instr)
        }
    }

    suspend fun getImprovedPrompt(raw: String, answers: Map<Int, String>): String {
        return withContext(Dispatchers.IO) {
            api.getFinalPrompt(raw, answers)
        }
    }

    suspend fun updatePrefs(prompt: String, comment: String) {
        withContext(Dispatchers.IO) {
            val old = getInstructions()
            val newInstr = api.updateInstructions(old, prompt, comment)
            if (newInstr.isNotBlank()) {
                db.prefsDao().save(UserPreferences(1, newInstr))
            }
        }
    }
}
