package com.andriawan.moviedb.data.network

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MovieAPITest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var movieAPI: MovieAPI

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        movieAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MovieAPI::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getMovies discover returns successful response`() = runTest {
        val mockResponse = """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/backdrop.jpg",
                        "genre_ids": [28, 12],
                        "id": 123,
                        "original_language": "en",
                        "original_title": "Test Movie",
                        "overview": "Test overview",
                        "popularity": 100.5,
                        "poster_path": "/poster.jpg",
                        "release_date": "2024-01-01",
                        "title": "Test Movie",
                        "video": false,
                        "vote_average": 8.5,
                        "vote_count": 1000
                    }
                ],
                "total_pages": 10,
                "total_results": 200
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.discoverMovies(page = 1)

        assertEquals(1, response.page)
        assertEquals(1, response.results?.size)
        assertEquals(10, response.totalPages)
        assertEquals(200, response.totalResults)

        val movie = response.results?.first()
        assertEquals(123, movie?.id)
        assertEquals("Test Movie", movie?.title)
        assertEquals(8.5, movie?.voteAverage)
        assertFalse(movie?.adult ?: true)
    }

    @Test
    fun `getMovies search returns successful response`() = runTest {
        val mockResponse = """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "id": 456,
                        "title": "Searched Movie",
                        "vote_average": 7.5
                    }
                ],
                "total_pages": 5,
                "total_results": 100
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.searchMovies(page = 1, query = "test")

        assertEquals(1, response.page)
        assertEquals(1, response.results?.size)
        assertEquals("Searched Movie", response.results?.first()?.title)
    }

    @Test
    fun `getMovies with includeAdult parameter`() = runTest {
        val mockResponse = """
            {
                "page": 1,
                "results": [],
                "total_pages": 0,
                "total_results": 0
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        movieAPI.discoverMovies(page = 1, includeAdult = true)

        val request = mockWebServer.takeRequest()
        assert(request.path?.contains("include_adult=true") == true)
    }

    @Test
    fun `getMovies returns empty results`() = runTest {
        val mockResponse = """
            {
                "page": 1,
                "results": [],
                "total_pages": 0,
                "total_results": 0
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.discoverMovies(page = 1)

        assertEquals(0, response.results?.size)
        assertEquals(0, response.totalResults)
    }
}