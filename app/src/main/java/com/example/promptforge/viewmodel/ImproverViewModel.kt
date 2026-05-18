package com.example.promptforge.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.promptforge.api.QuestionItem
import com.example.promptforge.model.Prompt
import com.example.promptforge.repository.AppRepository
import kotlinx.coroutines.launch

class ImproverViewModel(app: Application) : AndroidViewModel(app) {
    val repo = AppRepository(app)

    var step = mutableStateOf(0)
    var rawPrompt = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var questions = mutableStateOf<List<QuestionItem>>(emptyList())
    var answers = mutableStateMapOf<Int, String>()
    var improvedPrompt = mutableStateOf("")
    var rating = mutableStateOf(0)
    var comment = mutableStateOf("")
    var errorMsg = mutableStateOf("")

    fun improve() {
        if (rawPrompt.value.isBlank()) return
        isLoading.value = true
        errorMsg.value = ""
        viewModelScope.launch {
            try {
                val q = repo.getQuestions(rawPrompt.value)
                questions.value = q
                step.value = 1
            } catch (e: Exception) {
                errorMsg.value = "Network error: ${e.message}"
            }
            isLoading.value = false
        }
    }

    fun selectAnswer(qIdx: Int, ans: String) {
        answers[qIdx] = ans
    }

    fun generate() {
        isLoading.value = true
        errorMsg.value = ""
        viewModelScope.launch {
            try {
                val result = repo.getImprovedPrompt(rawPrompt.value, answers.toMap())
                improvedPrompt.value = result
                step.value = 2
            } catch (e: Exception) {
                errorMsg.value = "Network error: ${e.message}"
            }
            isLoading.value = false
        }
    }

    fun save() {
        viewModelScope.launch {
            val p = Prompt(
                raw_prompt = rawPrompt.value,
                improved_prompt = improvedPrompt.value,
                rating = rating.value,
                timestamp = System.currentTimeMillis()
            )
            repo.savePrompt(p)
            if (comment.value.isNotBlank()) {
                repo.updatePrefs(rawPrompt.value, comment.value)
            }
            reset()
        }
    }

    fun reset() {
        step.value = 0
        rawPrompt.value = ""
        isLoading.value = false
        questions.value = emptyList()
        answers.clear()
        improvedPrompt.value = ""
        rating.value = 0
        comment.value = ""
        errorMsg.value = ""
    }
}
