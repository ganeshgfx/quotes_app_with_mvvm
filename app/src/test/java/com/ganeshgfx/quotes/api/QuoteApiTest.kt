package com.ganeshgfx.quote

import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.models.Result
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var api: QuoteService

    val gson = Gson()

    val testResult = Result(
        _id = "test",
        author = "test",
        authorSlug = "test",
        content = "test",
        dateAdded = "test",
        dateModified = "test",
        length = 4
    )

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        api = Retrofit
            .Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(QuoteService::class.java)
    }

    @Test
    fun `Test getQuotes for success`()= runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody(gson.toJson(testResult))
        mockWebServer.enqueue(mockResponse)

        val response = api.getRandomQuote()
        mockWebServer.takeRequest()

        println(response.body())

        assertEquals(true, response.body()?.content != null)
    }

    @Test
    fun `Test getQuotes for error`()= runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("{}")
        mockWebServer.enqueue(mockResponse)

        val response = api.getRandomQuote()
        mockWebServer.takeRequest()

        println(response.body())

        assertEquals(true, response.body()?.content == null)
    }

    @Test
    fun `Test getQuotes for 404 error`()= runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("error")
        mockResponse.setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = api.getRandomQuote()
        mockWebServer.takeRequest()

        println(response.body())

        assertEquals(false, response.isSuccessful)
        assertEquals(404, response.code())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}