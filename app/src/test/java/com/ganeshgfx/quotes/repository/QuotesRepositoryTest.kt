package com.ganeshgfx.quotes.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.getOrAwaitValue
import com.ganeshgfx.quotes.room.QuoteDao
import com.ganeshgfx.quotes.room.QuoteDatabase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.ganeshgfx.quotes.models.Result
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class QuotesRepositoryTest {

    //A JUnit Test Rule that swaps the background executor used by the Architecture Components with a different one which executes each task synchronously. USE it or else get looper error with live data
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    lateinit var quotesRepository: QuotesRepository

    lateinit var api: QuoteService
    lateinit var db: QuoteDatabase
    lateinit var dao: QuoteDao
    lateinit var context: Context
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


    @Before
    fun setUp() {

        db = mock(QuoteDatabase::class.java)
        api = mock(QuoteService::class.java)
        context = mock(Context::class.java)

        dao = mock {
            onBlocking { getQuotes() } doReturn quotes
        }
        `when`(db.quoteDao()).thenReturn(dao)

        quotesRepository = QuotesRepository(
            api,
            db,
            context
        )

    }

    @Test
    fun testGetQuotes_ExpectList() = runBlocking {
        val result = quotesRepository.getQuotes().getOrAwaitValue()
        assertEquals(result.results.size, 3)
    }
}