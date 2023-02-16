package com.ganeshgfx.quotes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.models.QuoteList
import com.ganeshgfx.quotes.room.QuoteDatabase
import com.ganeshgfx.quotes.utils.NetworkUtils

class QuotesRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val context: Context
) {

    private val quotesLiveData = MutableLiveData<QuoteList>()

    val quotes: LiveData<QuoteList>
        get() = quotesLiveData

    suspend fun getQuotes(page: Int) {

        if(NetworkUtils.isOnline(context)){
            val result = quoteService.getQuotes(page)
            if (result?.body() != null) {
                quoteDatabase.quoteDao().addQuates(result.body()!!.results)
                quotesLiveData.postValue(result.body())
            }
        }else{
           val quotes = quoteDatabase.quoteDao().getQuotes()
            val quotesList = QuoteList(1,1,1,quotes,1,1)
            quotesLiveData.postValue(quotesList)
        }

    }
}