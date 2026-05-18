package com.example.promptforge.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.promptforge.model.Prompt
import com.example.promptforge.repository.AppRepository
import kotlinx.coroutines.launch

class DetailsViewModel(app: Application) : AndroidViewModel(app) {
    val repo = AppRepository(app)
    var prompt = mutableStateOf<Prompt?>(null)

    fun load(id: Int) {
        viewModelScope.launch {
            prompt.value = repo.getPrompt(id)
        }
    }
}
