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

    @Test
    fun `getMovieDetail returns successful response with complete data`() = runTest {
        val mockResponse = """
            {
                "adult": false,
                "backdrop_path": "/backdrop.jpg",
                "budget": 200000000,
                "genres": [
                    {
                        "id": 28,
                        "name": "Action"
                    },
                    {
                        "id": 12,
                        "name": "Adventure"
                    }
                ],
                "homepage": "https://example.com",
                "id": 123,
                "imdb_id": "tt1234567",
                "original_language": "en",
                "original_title": "Original Test Movie",
                "overview": "This is a test movie overview",
                "popularity": 150.5,
                "poster_path": "/poster.jpg",
                "production_companies": [
                    {
                        "id": 1,
                        "name": "Test Studios",
                        "logo_path": "/logo.jpg",
                        "origin_country": "US"
                    }
                ],
                "production_countries": [
                    {
                        "iso_3166_1": "US",
                        "name": "United States of America"
                    }
                ],
                "release_date": "2024-01-01",
                "revenue": 500000000,
                "runtime": 120,
                "spoken_languages": [
                    {
                        "iso_639_1": "en",
                        "name": "English"
                    }
                ],
                "status": "Released",
                "tagline": "An epic test movie",
                "title": "Test Movie",
                "video": false,
                "vote_average": 8.5,
                "vote_count": 5000
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.getMovieDetail(123)

        assertEquals(123, response.id)
        assertEquals("Test Movie", response.title)
        assertEquals("Original Test Movie", response.originalTitle)
        assertEquals("This is a test movie overview", response.overview)
        assertEquals(8.5, response.voteAverage)
        assertEquals(5000, response.voteCount)
        assertEquals(120, response.runtime)
        assertEquals(200000000, response.budget)
        assertEquals(500000000, response.revenue)
        assertEquals("Released", response.status)
        assertEquals("An epic test movie", response.tagline)
        assertEquals("tt1234567", response.imdbId)
        assertEquals("https://example.com", response.homepage)
        assertEquals(false, response.adult)
        assertEquals(2, response.genres?.size)
        assertEquals("Action", response.genres?.get(0)?.name)
        assertEquals("Adventure", response.genres?.get(1)?.name)
    }

    @Test
    fun `getMovieDetail returns successful response with minimal data`() = runTest {
        val mockResponse = """
            {
                "id": 456,
                "title": "Minimal Movie"
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.getMovieDetail(456)

        assertEquals(456, response.id)
        assertEquals("Minimal Movie", response.title)
    }

    @Test
    fun `getMovieDetail with correct path parameter`() = runTest {
        val mockResponse = """
            {
                "id": 789,
                "title": "Path Test Movie"
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        movieAPI.getMovieDetail(789)

        val request = mockWebServer.takeRequest()
        assert(request.path?.contains("movie/789") == true)
    }

    @Test
    fun `getMovieCredits returns successful response with cast`() = runTest {
        val mockResponse = """
            {
                "id": 123,
                "cast": [
                    {
                        "cast_id": 1,
                        "character": "Hero",
                        "id": 100,
                        "name": "John Doe",
                        "order": 0,
                        "original_name": "John Doe",
                        "popularity": 50.5,
                        "profile_path": "/profile1.jpg"
                    },
                    {
                        "cast_id": 2,
                        "character": "Villain",
                        "id": 101,
                        "name": "Jane Smith",
                        "order": 1,
                        "original_name": "Jane Smith",
                        "popularity": 45.3,
                        "profile_path": "/profile2.jpg"
                    }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.getMovieCredits(123)

        assertEquals(123, response.id)
        assertEquals(2, response.cast?.size)

        val firstCast = response.cast?.get(0)
        assertEquals(1, firstCast?.castId)
        assertEquals("Hero", firstCast?.character)
        assertEquals(100, firstCast?.id)
        assertEquals("John Doe", firstCast?.name)
        assertEquals(0, firstCast?.order)
        assertEquals(50.5, firstCast?.popularity)
        assertEquals("/profile1.jpg", firstCast?.profilePath)

        val secondCast = response.cast?.get(1)
        assertEquals("Villain", secondCast?.character)
        assertEquals("Jane Smith", secondCast?.name)
        assertEquals(1, secondCast?.order)
    }

    @Test
    fun `getMovieCredits returns empty cast list`() = runTest {
        val mockResponse = """
            {
                "id": 456,
                "cast": []
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.getMovieCredits(456)

        assertEquals(456, response.id)
        assertEquals(0, response.cast?.size)
    }

    @Test
    fun `getMovieCredits with null cast`() = runTest {
        val mockResponse = """
            {
                "id": 789
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = movieAPI.getMovieCredits(789)

        assertEquals(789, response.id)
        assertEquals(null, response.cast)
    }

    @Test
    fun `getMovieCredits with correct path parameter`() = runTest {
        val mockResponse = """
            {
                "id": 999,
                "cast": []
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        movieAPI.getMovieCredits(999)

        val request = mockWebServer.takeRequest()
        assert(request.path?.contains("movie/999/credits") == true)
    }
}