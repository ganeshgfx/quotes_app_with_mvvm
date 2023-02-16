package com.ganeshgfx.quotes

import android.app.Application
import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.api.RetrofitHelper
import com.ganeshgfx.quotes.repository.QuotesRepository
import com.ganeshgfx.quotes.room.QuoteDatabase

class QuoteApplication : Application(){

    lateinit var quotesRepository : QuotesRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val database = QuoteDatabase.getDatabase(applicationContext)
        quotesRepository = QuotesRepository(quoteService,database,applicationContext)
    }
}