package com.ganeshgfx.quotes.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.ganeshgfx.quotes.QuoteApplication
import com.ganeshgfx.quotes.getOrAwaitValue
import com.ganeshgfx.quotes.models.QuoteList
import com.ganeshgfx.quotes.models.Result
import com.ganeshgfx.quotes.repository.QuotesRepository
import com.nhaarman.mockitokotlin2.doReturn
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuotesViewModelTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    lateinit var application: MockApp

    lateinit var viewModel: QuotesViewModel

    lateinit var repository: QuotesRepository

    val testResult = Result(
        _id = "test",
        author = "test",
        authorSlug = "test",
        content = "test",
        dateAdded = "test",
        dateModified = "test",
        length = 4
    )
    val testResult2 = Result(
        _id = "test2",
        author = "test2",
        authorSlug = "test2",
        content = "test2",
        dateAdded = "test2",
        dateModified = "test2",
        length = 5
    )

    val emptyQuotesList = QuoteList(
        count = 0,
        lastItemIndex = 0,
        page = 0,
        results = emptyList(),
        totalCount = 0,
        totalPages = 0
    )

    val quotesList = QuoteList(
        count = 0,
        lastItemIndex = 0,
        page = 0,
        results = listOf(testResult, testResult2),
        totalCount = 0,
        totalPages = 0
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        application = mock(MockApp::class.java)
        repository = mock(QuotesRepository::class.java)

        `when`(repository.randomQuote).thenReturn(MutableLiveData(testResult))
        `when`(repository.quotes).thenReturn(MutableLiveData(quotesList))

        `when`(application.quotesRepository).thenReturn(repository)

        viewModel = QuotesViewModel(application)
    }

    @Test
    fun `Test refreshQuote`() = runBlocking {
        val oldValue = viewModel.randomQuote.getOrAwaitValue()
        `when`(repository.getRandomQuote()).then {
            `when`(repository.randomQuote).thenReturn(MutableLiveData(testResult2))
        }
        viewModel.refreshQuote()
        val newValue = viewModel.randomQuote.getOrAwaitValue()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotEquals(oldValue, newValue)
    }

    @Test
    fun `Test clearQuotes`() = runBlocking {
        val oldValue = viewModel.quotes.getOrAwaitValue()
        `when`(repository.clearQuotes()).then {
            `when`(repository.quotes).thenReturn(MutableLiveData(emptyQuotesList))
        }
        viewModel.clearQuotes()
        val newValue = viewModel.quotes.getOrAwaitValue()
        println("$oldValue \n\n $newValue")
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotEquals(oldValue, newValue)
    }

    @Test
    fun `Test copyText`() = runTest{
        `when`(application.copyText("test ~ test")).thenReturn("test ~ test")
        val result = viewModel.copyText()
        println(result)
        assertTrue(result)
    }

    @Test
    fun `Test onRefresh`() = runTest{
        viewModel.onRefresh()
        val result = viewModel.isLoading.getOrAwaitValue()
        assertFalse(result)
    }

    @Test
    fun `Test getTextToShare`(){
        assertEquals(
           viewModel.getTextToShare("test","test"),
           "test ~ test"
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}