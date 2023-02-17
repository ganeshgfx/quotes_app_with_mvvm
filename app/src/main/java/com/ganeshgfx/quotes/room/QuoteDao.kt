package com.ganeshgfx.quotes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ganeshgfx.quotes.models.Result

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuates(quotes: List<Result>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOneQuote(quote: Result)

    @Query("SELECT * FROM quote")
    suspend fun getQuotes() : List<Result>
}