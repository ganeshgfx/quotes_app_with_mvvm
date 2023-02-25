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
    private val context: Context
) {

    private val quotesLiveData = MutableLiveData<QuoteList>()
    private val _randomQuote = MutableLiveData(Result("", "", "", "", "", "", -99))

    val quotes: LiveData<QuoteList>
        get() = quotesLiveData

    val randomQuote: LiveData<Result>
        get() = _randomQuote


    suspend fun getQuotes() {

        val quotes = quoteDatabase.quoteDao().getQuotes()
        val quotesList = QuoteList(1, 1, 1, quotes, 1, 1)
        quotesLiveData.postValue(quotesList)

//        if (NetworkUtils.isOnline(context)) {
//            val result = quoteService.getQuotes(page)
//            if (result?.body() != null) {
//                quoteDatabase.quoteDao().addQuates(result.body()!!.results)
//                quotesLiveData.postValue(result.body())
//            }
//        } else {
//            val quotes = quoteDatabase.quoteDao().getQuotes()
//            val quotesList = QuoteList(1, 1, 1, quotes, 1, 1)
//            quotesLiveData.postValue(quotesList)
//        }

    }

    suspend fun getRandomQuote() {
        if (NetworkUtils.isOnline(context)) {
            val quote = quoteService.getRandomQuote()
            if (quote != null) {
                val r: Result? = quote.body()
                if (r != null) {
                    _randomQuote.postValue(r)
                }
            }
        } else {
            val quote = quoteDatabase.quoteDao().getQuotes().let {
                it[(it.indices).random()]
            }
            _randomQuote.postValue(quote)
        }

    }

    suspend fun addOldRandomQuoteToDb(){
        randomQuote.value?.let {
            if (it.length != -99) {
                quoteDatabase.quoteDao().addOneQuote(it)
            }
        }
    }

    suspend fun clearQuotes() {
        //_randomQuote.value?.let { quoteDatabase.quoteDao().clearQuotes(it._id) }
        quoteDatabase.quoteDao().clearQuotesAll()
        getQuotes()
    }
}