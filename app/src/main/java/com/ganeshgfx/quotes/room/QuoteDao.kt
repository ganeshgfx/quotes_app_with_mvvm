package com.ganeshgfx.quotes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ganeshgfx.quotes.models.Result

@Dao
interface QuoteDao {
    @Insert
    suspend fun addQuates(quotes: List<Result>)

    @Query("SELECT * FROM quote")
    suspend fun getQuotes() : List<Result>
}