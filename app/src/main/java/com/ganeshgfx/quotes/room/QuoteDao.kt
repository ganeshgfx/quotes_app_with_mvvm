package com.ganeshgfx.quotes.room

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM quote")
    fun getQuotesLiveData() : LiveData<List<Result>>

    @Query("SELECT * FROM quote WHERE _id == :id")
    suspend fun getOneQuote(id:String) : Result

    @Query("DELETE from quote WHERE _id != :id")
    suspend fun clearQuotes(id:String)

    @Query("DELETE from quote WHERE 1")
    suspend fun clearQuotesAll()
}