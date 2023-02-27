package com.ganeshgfx.quotes.viewmodels

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ganeshgfx.quotes.QuoteApplication
import com.ganeshgfx.quotes.models.QuoteList
import com.ganeshgfx.quotes.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuotesViewModel(application: Application) : AndroidViewModel(application) {

    private val app: QuoteApplication = application as QuoteApplication
    private val repository = app.quotesRepository

    val quotes: LiveData<QuoteList>
        get() = repository.quotes

    val randomQuote: LiveData<Result>
        get() = repository.randomQuote

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRandomQuote()
            repository.getQuotes()
        }
    }

    suspend fun refreshQuote() {
        repository.getRandomQuote()
        repository.addOldRandomQuoteToDb()
        repository.getQuotes()
    }

    suspend fun clearQuotes(){
        repository.clearQuotes()
        repository.getQuotes()
    }

    fun copyText(): Boolean {
        val textToCopy = randomQuote.value?.let {
            getTextToShare(
                it.content,
                it.author
            )
        }
        val result = textToCopy?.let { app.copyText(it) }
        return textToCopy == result
    }

    var isLoading = MutableLiveData(false)
    fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            refreshQuote()
            isLoading.postValue(false)
        }
    }

    fun getTextToShare(content:String,author:String) = "$content ~ $author"
}