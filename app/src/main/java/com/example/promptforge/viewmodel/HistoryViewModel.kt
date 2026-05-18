package com.example.promptforge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.promptforge.model.Prompt
import com.example.promptforge.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(app: Application) : AndroidViewModel(app) {
    val repo = AppRepository(app)

    val prompts = repo.getPrompts().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun delete(p: Prompt) {
        viewModelScope.launch {
            repo.deletePrompt(p)
        }
    }
}
