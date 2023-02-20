package com.ganeshgfx.quotes.viewmodels

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    suspend fun clearQuotes(){
        repository.clearQuotes()
        repository.getQuotes()
    }

    fun copyText(view:View):Boolean{
        val textToCopy = "${randomQuote.value?.content} ~ ${randomQuote.value?.author}"
        val clipboardManager = view.context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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