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
import com.ganeshgfx.quotes.utils.NetworkUtils
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class QuotesRepositoryTest {

    //A JUnit Test Rule that swaps the background executor used by the Architecture Components with a different one which executes each task synchronously. USE it or else get looper error with live data
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var quotesRepository: QuotesRepository

    @Mock
    lateinit var api: QuoteService
    @Mock
    lateinit var db: QuoteDatabase
    @Mock
    lateinit var dao: QuoteDao
    @Mock
    lateinit var context: Context
    @Mock
    lateinit var networkUtils : NetworkUtils

    private val quotes: List<Result> = listOf(
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
        MockitoAnnotations.openMocks(this)
    }
    private suspend fun iniDefault() {
        dao = mock {
            onBlocking { getQuotes() } doReturn quotes
        }
        `when`(db.quoteDao()).thenReturn(dao)

        val apiResponse = Response.success(Result(
            _id = "test",
            author = "test",
            authorSlug = "test",
            content = "test",
            dateAdded = "test",
            dateModified = "test",
            length = 5
        ))
        `when`(api.getRandomQuote()).thenReturn(apiResponse)
        quotesRepository = QuotesRepository(
            api,
            db,
            context,
            networkUtils
        )
    }
    private fun iniEmptyDB() {
        dao = mock {
            onBlocking { getQuotes() } doReturn emptyList()
        }
        `when`(db.quoteDao()).thenReturn(dao)
        quotesRepository = QuotesRepository(
            api,
            db,
            context,
            networkUtils
        )
    }

    @Test
    fun testGetQuotes_ExpectList() = runBlocking {
       iniDefault()

        val result = quotesRepository.getQuotes().getOrAwaitValue()
        val liveData = quotesRepository.quotes.getOrAwaitValue ()
        assertEquals(liveData.results.size, 3)
        assertEquals(result.results.size, 3)
    }
    @Test
    fun testGetQuotes_ExpectEmptyList() = runBlocking {
        iniEmptyDB()

        val result = quotesRepository.getQuotes().getOrAwaitValue()
        val liveData = quotesRepository.quotes.getOrAwaitValue ()
        assertEquals(liveData.results.size, 0)
        assertEquals(result.results.size, 0)
    }

    @Test fun testGetRandomQuote_whenNoData()= runBlocking{
        iniEmptyDB()
        dao = mock {
            onBlocking { getQuotes() } doReturn emptyList<Result>()
        }
        `when`(networkUtils.isOnline(context)).thenReturn(false)
        assertEquals(networkUtils.isOnline(context),false)
        val result = quotesRepository.getRandomQuote()
        assertEquals(result?.content,"")
        val liveData = quotesRepository.randomQuote.getOrAwaitValue()
        assertEquals(liveData.content,"")
    }


    @Test fun testGetRandomQuote_whenOffline()= runBlocking{
        iniDefault()
        `when`(networkUtils.isOnline(context)).thenReturn(false)
        assertEquals(networkUtils.isOnline(context),false)
        val result = quotesRepository.getRandomQuote()
        assertEquals(result?.content,"test")
        val liveData = quotesRepository.randomQuote.getOrAwaitValue()
        assertEquals(liveData.content,"test")
    }

    @Test fun testGetRandomQuote_whenOnline()= runBlocking{
        iniDefault()
        `when`(networkUtils.isOnline(context)).thenReturn(true)
        assertEquals(networkUtils.isOnline(context),true)
        val result = quotesRepository.getRandomQuote()
        assertEquals(result?.content,"test")
        val liveData = quotesRepository.randomQuote.getOrAwaitValue()
        assertEquals(liveData.content,"test")
    }

    @Test fun testAddOldRandomQuoteToDb() = runBlocking{
        iniDefault()
        `when`(networkUtils.isOnline(context)).thenReturn(true)
        assertEquals(networkUtils.isOnline(context),true)
        quotesRepository.getRandomQuote()
        assertEquals(quotesRepository.addOldRandomQuoteToDb(),true)
    }

    @Test fun testAddOldRandomQuoteToDb_whenNoData() = runBlocking{
        iniEmptyDB()
        `when`(networkUtils.isOnline(context)).thenReturn(true)
        assertEquals(networkUtils.isOnline(context),true)
        assertEquals(quotesRepository.addOldRandomQuoteToDb(),false)
    }
    @Test fun testClearQuotes() = runBlocking{
        iniEmptyDB()
        dao = mock {
            onBlocking { clearQuotesAll() } doReturn (Unit)
        }
        quotesRepository.clearQuotes()
        val liveData = quotesRepository.getQuotes().getOrAwaitValue()
        assertEquals(liveData.results.size,0)
    }

}