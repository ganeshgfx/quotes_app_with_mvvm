package com.ganeshgfx.quotes

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.api.RetrofitHelper
import com.ganeshgfx.quotes.repository.QuotesRepository
import com.ganeshgfx.quotes.room.QuoteDatabase
import com.ganeshgfx.quotes.utils.NetworkUtils

open class QuoteApplication : Application(){

    lateinit var quotesRepository : QuotesRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val database = QuoteDatabase.getDatabase(applicationContext)
        quotesRepository = QuotesRepository(quoteService,database,applicationContext, NetworkUtils())
    }

    open fun copyText(textToCopy: String):String {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        return clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
    }
}