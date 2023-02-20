package com.ganeshgfx.quotes.viewmodels

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
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

    private val app : QuoteApplication = application as QuoteApplication
    private val repository = app.quotesRepository

    val quotes: LiveData<QuoteList>
        get() = repository.quotes

    val randomQuote: LiveData<Result>
        get() = repository.randomQuote

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes()
            repository.getRandomQuote()
        }
    }

    suspend fun refreshQuote() {
        repository.getRandomQuote()
        repository.getQuotes()
    }

    suspend fun clearQuotes(){
        repository.clearQuotes()
        repository.getQuotes()
    }

    fun copyText():Boolean{
        val textToCopy = "${randomQuote.value?.content} ~ ${randomQuote.value?.author}"
        val clipboardManager = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        return true
    }

    var isLoading = MutableLiveData(false)

    fun onRefresh(){
        viewModelScope.launch(Dispatchers.IO){
            refreshQuote()
            isLoading.postValue(false)
        }
    }

}