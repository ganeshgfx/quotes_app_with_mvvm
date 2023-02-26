package com.ganeshgfx.quotes.viewmodels

import com.ganeshgfx.quotes.repository.QuotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class QuotesViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private var testDispacher = StandardTestDispatcher()

    @Mock
    lateinit var repository: QuotesRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        TestScope().launch {
            //Mockito.`when`(repository.getRandomQuote()).then()
        }
        //Dispatchers.setMain(testDispacher)
    }

    @Test fun testGetRandomQuote(){

    }

    @Test
    fun test_refreshQuote(){

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}