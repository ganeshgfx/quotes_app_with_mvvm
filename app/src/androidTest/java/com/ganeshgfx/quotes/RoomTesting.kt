package com.ganeshgfx.quotes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ganeshgfx.quotes.models.Result
import com.ganeshgfx.quotes.room.QuoteDao
import com.ganeshgfx.quotes.room.QuoteDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoomTesting {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    lateinit var quoteDatabase: QuoteDatabase
    lateinit var quoteDao: QuoteDao

    @Before
    fun setUp() {
        quoteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            QuoteDatabase::class.java
        ).allowMainThreadQueries().build()
        quoteDao = quoteDatabase.quoteDao()
    }

    @Test
    fun addQuates_expectInserted_Normal() = runBlocking {
        val quotes: List<Result> = listOf(
            Result(
                _id = "1",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "3",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            )
        )
        quoteDao.addQuates(quotes)
        val result = quoteDao.getQuotes()
        assertEquals(3, result.size)
    }

    @Test
    fun addQuates_WithConflict_expectInserted_Normal() = runBlocking {
        val quotes: List<Result> = listOf(
            Result(
                _id = "1",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            )
        )
        quoteDao.addQuates(quotes)
        val result = quoteDao.getQuotes()
        assertEquals(2, result.size)
    }

    @Test
    fun insertRandomQuote_expectInserted_Normal() = runBlocking {
        val quote = Result(
            _id = "sdfsd",
            author = "test",
            authorSlug = "test",
            content = "test",
            dateAdded = "test",
            dateModified = "test",
            length = 4
        )
        quoteDao.addOneQuote(quote)
        val result = quoteDao.getQuotes()
        assertEquals(result.size, 1)
    }

    @Test
    fun insertRandomQuote_expectInserted_LiveData() = runBlocking {
        val quote = Result(
            _id = "test",
            author = "test",
            authorSlug = "test",
            content = "test is test",
            dateAdded = "test",
            dateModified = "test",
            length = 4
        )
        quoteDao.addOneQuote(quote)

        val result = quoteDao.getQuotesLiveData().getOrAwaitValue()

        assertEquals(result.size, 1)
        assertEquals(result[0].content, quote.content)
    }

    @Test fun getOneQuote_expectInserted_Normal() = runBlocking {
        val quotes: List<Result> = listOf(
            Result(
                _id = "1",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "3",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "9",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            )
        )
        quoteDao.addQuates(quotes)
        val result = quoteDao.getOneQuote("9")
        assertEquals("9", result._id)
    }

    @Test fun clearQuotes_expectClearedExpectOne() = runBlocking {
        val quotes: List<Result> = listOf(
            Result(
                _id = "1",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "3",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "9",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            )
        )
        quoteDao.addQuates(quotes)
        quoteDao.clearQuotes("9")
        val result = quoteDao.getOneQuote("9")
        assertEquals("9", result._id)
    }

    @Test fun clearQuotes_withCorrecId_expectALLDeletedExpectOne() = runBlocking {
        val quotes: List<Result> = listOf(
            Result(
                _id = "1",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "3",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "9",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            )
        )
        quoteDao.addQuates(quotes)
        quoteDao.clearQuotes("9")
        val result = quoteDao.getQuotes()
        assertEquals(1, result.size)
    }

    @Test fun clearQuotes_withWrongId_expectALLDeleted() = runBlocking {
        val quotes: List<Result> = listOf(
            Result(
                _id = "1",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "3",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "9",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            )
        )
        quoteDao.addQuates(quotes)
        quoteDao.clearQuotes("99")
        val result = quoteDao.getQuotes()
        assertEquals(0, result.size)
    }

    @Test fun clearQuotes_expectNothingInDB() = runBlocking {
        val quotes: List<Result> = listOf(
            Result(
                _id = "1",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "2",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "3",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            ),
            Result(
                _id = "9",
                author = "test",
                authorSlug = "test",
                content = "test",
                dateAdded = "test",
                dateModified = "test",
                length = 4
            )
        )
        quoteDao.addQuates(quotes)
        quoteDao.clearQuotesAll()
        val result = quoteDao.getQuotes()
        assertEquals(0, result.size)
    }

    @After
    fun tearDown() {
        quoteDatabase.close()
    }
}