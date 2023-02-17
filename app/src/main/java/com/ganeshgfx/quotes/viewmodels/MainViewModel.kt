package com.ganeshgfx.quotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganeshgfx.quotes.models.QuoteList
import com.ganeshgfx.quotes.repository.QuotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: QuotesRepository) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes()
            repository.getRandomQuote()
        }
    }

    val quotes: LiveData<QuoteList>
        get() = repository.quotes

    val randomQuote: LiveData<com.ganeshgfx.quotes.models.Result>
        get() = repository.randomQuote

    suspend fun refreshQuote() {
        repository.getRandomQuote()
        repository.getQuotes()
    }
}