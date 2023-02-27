package com.ganeshgfx.quotes.viewmodels

import com.ganeshgfx.quotes.QuoteApplication
import com.ganeshgfx.quotes.repository.QuotesRepository

lateinit var quotesRepository : QuotesRepository

class MockApp : QuoteApplication() {
    override fun copyText(data:String):String{
        return data
    }
}