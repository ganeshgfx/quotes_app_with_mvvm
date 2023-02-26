package com.ganeshgfx.quotes.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.models.QuoteList
import com.ganeshgfx.quotes.room.QuoteDatabase
import com.ganeshgfx.quotes.utils.NetworkUtils
import com.ganeshgfx.quotes.models.Result

class QuotesRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val context: Context,
    private val networkUtils:NetworkUtils
) {

    private val quotesLiveData = MutableLiveData<QuoteList>()
    private val _randomQuote = MutableLiveData(Result("", "", "", "", "", "", -99))

    val quotes: LiveData<QuoteList>
        get() = quotesLiveData

    val randomQuote: LiveData<Result>
        get() = _randomQuote


    suspend fun getQuotes(): LiveData<QuoteList> {

        val quotes = quoteDatabase.quoteDao().getQuotes()
        val quotesList = QuoteList(1, 1, 1, quotes, 1, 1)
        quotesLiveData.postValue(quotesList)
        return quotesLiveData

    }

    suspend fun getRandomQuote(): Result? {
        if (networkUtils.isOnline(context)) {
            val quote = quoteService.getRandomQuote()
            val r: Result? = quote.body()
            if (r != null) {
                _randomQuote.postValue(r)
                return r
            }
        }
        val dbQuote = quoteDatabase.quoteDao().getQuotes()
        if(dbQuote.size>1){
            val r = dbQuote.let {
                it[(it.indices).random()]
            }
            _randomQuote.postValue(r)
            return r
        }
        return randomQuote.value
    }

    suspend fun addOldRandomQuoteToDb():Boolean {
        randomQuote.value?.let {
            if (it.length != -99) {
                quoteDatabase.quoteDao().addOneQuote(it)
                return true
            }
        }
        return false
    }

    suspend fun clearQuotes() {
        quoteDatabase.quoteDao().clearQuotesAll()
        getQuotes()
    }
}