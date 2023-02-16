package com.ganeshgfx.quotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ganeshgfx.quotes.repository.QuotesRepository

class MainViewModelFactory(private val repository: QuotesRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}