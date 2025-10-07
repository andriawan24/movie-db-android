package com.andriawan.moviedb.data.repository

import com.andriawan.moviedb.data.network.MovieAPI
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.data.responses.MovieResponse
import com.andriawan.moviedb.data.responses.PaginationMovieResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MovieRepositoryImplTest {

    private lateinit var movieAPI: MovieAPI
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        movieAPI = mock()
        repository = MovieRepositoryImpl(movieAPI)
    }

    @Test
    fun `getMovies with query calls search API`() = runTest {
        val request = GetMovieListRequest(query = "test", page = 1)
        val mockResponse = PaginationMovieResponse(
            page = 1,
            results = listOf(
                MovieResponse(
                    id = 123,
                    title = "Test Movie",
                    voteAverage = 8.0
                )
            ),
            totalPages = 5,
            totalResults = 100
        )

        whenever(movieAPI.getMovies(1, "test")).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        verify(movieAPI).getMovies(1, "test")
        assertEquals(1, result.page)
        assertEquals(1, result.results.size)
        assertEquals(123, result.results.first().id)
        assertEquals("Test Movie", result.results.first().title)
        assertEquals(8.0, result.results.first().voteAverage, 0.01)
    }

    @Test
    fun `getMovies without query calls discover API`() = runTest {
        val request = GetMovieListRequest(query = null, page = 2)
        val mockResponse = PaginationMovieResponse(
            page = 2,
            results = listOf(
                MovieResponse(
                    id = 456,
                    title = "Another Movie",
                    voteAverage = 7.5
                )
            ),
            totalPages = 10,
            totalResults = 200
        )

        whenever(movieAPI.getMovies(2)).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        verify(movieAPI).getMovies(2)
        assertEquals(2, result.page)
        assertEquals(1, result.results.size)
        assertEquals(456, result.results.first().id)
    }

    @Test
    fun `getMovies returns empty results when API returns null results`() = runTest {
        val request = GetMovieListRequest(query = null, page = 1)
        val mockResponse = PaginationMovieResponse(
            page = 1,
            results = null,
            totalPages = 0,
            totalResults = 0
        )

        whenever(movieAPI.getMovies(1)).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        assertEquals(0, result.results.size)
        assertEquals(0, result.totalResults)
    }

    @Test
    fun `getMovies maps response correctly to domain model`() = runTest {
        val request = GetMovieListRequest(query = null, page = 1)
        val mockResponse = PaginationMovieResponse(
            page = 1,
            results = listOf(
                MovieResponse(
                    adult = false,
                    backdropPath = "/backdrop.jpg",
                    genreIds = listOf(28, 12),
                    id = 789,
                    originalLanguage = "en",
                    originalTitle = "Original Title",
                    overview = "Overview text",
                    popularity = 100.5,
                    posterPath = "/poster.jpg",
                    releaseDate = "2024-01-01",
                    title = "Display Title",
                    video = false,
                    voteAverage = 9.0,
                    voteCount = 5000
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        whenever(movieAPI.getMovies(1)).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        val movie = result.results.first()
        assertEquals(false, movie.adult)
        assertEquals("/backdrop.jpg", movie.backdropPath)
        assertEquals(listOf(28, 12), movie.genreIds)
        assertEquals(789, movie.id)
        assertEquals("en", movie.originalLanguage)
        assertEquals("Original Title", movie.originalTitle)
        assertEquals("Overview text", movie.overview)
        assertEquals(100.5, movie.popularity, 0.01)
        assertEquals("/poster.jpg", movie.posterPath)
        assertEquals("2024-01-01", movie.releaseDate)
        assertEquals("Display Title", movie.title)
        assertEquals(false, movie.video)
        assertEquals(9.0, movie.voteAverage, 0.01)
        assertEquals(5000, movie.voteCount)
    }

    @Test
    fun `getMovies handles multiple results`() = runTest {
        val request = GetMovieListRequest(query = null, page = 1)
        val mockResponse = PaginationMovieResponse(
            page = 1,
            results = listOf(
                MovieResponse(id = 1, title = "Movie 1"),
                MovieResponse(id = 2, title = "Movie 2"),
                MovieResponse(id = 3, title = "Movie 3")
            ),
            totalPages = 1,
            totalResults = 3
        )

        whenever(movieAPI.getMovies(1)).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        assertEquals(3, result.results.size)
        assertEquals("Movie 1", result.results[0].title)
        assertEquals("Movie 2", result.results[1].title)
        assertEquals("Movie 3", result.results[2].title)
    }
}