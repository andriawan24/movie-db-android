package com.andriawan.moviedb.data.repository

import com.andriawan.moviedb.data.network.MovieAPI
import com.andriawan.moviedb.data.requests.GetMovieDetailRequest
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.data.responses.CastResponse
import com.andriawan.moviedb.data.responses.CreditsResponse
import com.andriawan.moviedb.data.responses.GenreResponse
import com.andriawan.moviedb.data.responses.MovieDetailResponse
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

        whenever(movieAPI.searchMovies(1, "test")).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        verify(movieAPI).searchMovies(1, "test")
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

        whenever(movieAPI.discoverMovies(2)).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        verify(movieAPI).discoverMovies(2)
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

        whenever(movieAPI.discoverMovies(1)).thenReturn(mockResponse)

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

        whenever(movieAPI.discoverMovies(1)).thenReturn(mockResponse)

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

        whenever(movieAPI.discoverMovies(1)).thenReturn(mockResponse)

        val result = repository.getMovies(request)

        assertEquals(3, result.results.size)
        assertEquals("Movie 1", result.results[0].title)
        assertEquals("Movie 2", result.results[1].title)
        assertEquals("Movie 3", result.results[2].title)
    }

    @Test
    fun `getMovieDetail calls API and maps response correctly`() = runTest {
        val request = GetMovieDetailRequest(id = 123)
        val mockResponse = MovieDetailResponse(
            id = 123,
            title = "Test Movie",
            adult = false,
            backdropPath = "/backdrop.jpg",
            budget = 200000000,
            genres = listOf(
                GenreResponse(id = 28, name = "Action"),
                GenreResponse(id = 12, name = "Adventure")
            ),
            homepage = "https://example.com",
            imdbId = "tt1234567",
            originalLanguage = "en",
            originalTitle = "Original Test Movie",
            overview = "Test overview",
            popularity = 150.5,
            posterPath = "/poster.jpg",
            releaseDate = "2024-01-01",
            revenue = 500000000,
            runtime = 120,
            status = "Released",
            tagline = "An epic test movie",
            video = false,
            voteAverage = 8.5,
            voteCount = 5000
        )

        whenever(movieAPI.getMovieDetail(123)).thenReturn(mockResponse)

        val result = repository.getMovieDetail(request)

        verify(movieAPI).getMovieDetail(123)
        assertEquals(123, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals(false, result.adult)
        assertEquals("/backdrop.jpg", result.backdropPath)
        assertEquals(200000000, result.budget)
        assertEquals(2, result.genres.size)
        assertEquals("Action", result.genres[0].name)
        assertEquals("Adventure", result.genres[1].name)
        assertEquals("https://example.com", result.homepage)
        assertEquals("tt1234567", result.imdbId)
        assertEquals("en", result.originalLanguage)
        assertEquals("Original Test Movie", result.originalTitle)
        assertEquals("Test overview", result.overview)
        assertEquals(150.5, result.popularity, 0.01)
        assertEquals("/poster.jpg", result.posterPath)
        assertEquals("2024-01-01", result.releaseDate)
        assertEquals(500000000, result.revenue)
        assertEquals(120, result.runtime)
        assertEquals("Released", result.status)
        assertEquals("An epic test movie", result.tagline)
        assertEquals(false, result.video)
        assertEquals(8.5, result.voteAverage, 0.01)
        assertEquals(5000, result.voteCount)
    }

    @Test
    fun `getMovieDetail handles null values correctly`() = runTest {
        val request = GetMovieDetailRequest(id = 456)
        val mockResponse = MovieDetailResponse(
            id = 456,
            title = "Minimal Movie"
        )

        whenever(movieAPI.getMovieDetail(456)).thenReturn(mockResponse)

        val result = repository.getMovieDetail(request)

        assertEquals(456, result.id)
        assertEquals("Minimal Movie", result.title)
        assertEquals("", result.backdropPath)
        assertEquals(0, result.budget)
        assertEquals(0, result.genres.size)
        assertEquals("", result.homepage)
        assertEquals("", result.imdbId)
    }

    @Test
    fun `getMovieDetail filters null genres`() = runTest {
        val request = GetMovieDetailRequest(id = 789)
        val mockResponse = MovieDetailResponse(
            id = 789,
            title = "Genre Test",
            genres = listOf(
                GenreResponse(id = 28, name = "Action"),
                null,
                GenreResponse(id = 12, name = "Adventure")
            )
        )

        whenever(movieAPI.getMovieDetail(789)).thenReturn(mockResponse)

        val result = repository.getMovieDetail(request)

        assertEquals(2, result.genres.size)
        assertEquals("Action", result.genres[0].name)
        assertEquals("Adventure", result.genres[1].name)
    }

    @Test
    fun `getMovieCredits calls API and maps response correctly`() = runTest {
        val request = GetMovieDetailRequest(id = 123)
        val mockResponse = CreditsResponse(
            id = 123,
            cast = listOf(
                CastResponse(
                    castId = 1,
                    character = "Hero",
                    id = 100,
                    name = "John Doe",
                    order = 0,
                    originalName = "John Doe",
                    popularity = 50.5,
                    profilePath = "/profile1.jpg"
                ),
                CastResponse(
                    castId = 2,
                    character = "Villain",
                    id = 101,
                    name = "Jane Smith",
                    order = 1,
                    originalName = "Jane Smith",
                    popularity = 45.3,
                    profilePath = "/profile2.jpg"
                )
            )
        )

        whenever(movieAPI.getMovieCredits(123)).thenReturn(mockResponse)

        val result = repository.getMovieCredits(request)

        verify(movieAPI).getMovieCredits(123)
        assertEquals(123, result.id)
        assertEquals(2, result.cast.size)

        val firstCast = result.cast[0]
        assertEquals(1, firstCast.castId)
        assertEquals("Hero", firstCast.character)
        assertEquals(100, firstCast.id)
        assertEquals("John Doe", firstCast.name)
        assertEquals(0, firstCast.order)
        assertEquals("John Doe", firstCast.originalName)
        assertEquals(50.5, firstCast.popularity, 0.01)
        assertEquals("/profile1.jpg", firstCast.profilePath)

        val secondCast = result.cast[1]
        assertEquals("Villain", secondCast.character)
        assertEquals("Jane Smith", secondCast.name)
        assertEquals(1, secondCast.order)
    }

    @Test
    fun `getMovieCredits handles null cast`() = runTest {
        val request = GetMovieDetailRequest(id = 456)
        val mockResponse = CreditsResponse(
            id = 456,
            cast = null
        )

        whenever(movieAPI.getMovieCredits(456)).thenReturn(mockResponse)

        val result = repository.getMovieCredits(request)

        assertEquals(456, result.id)
        assertEquals(0, result.cast.size)
    }

    @Test
    fun `getMovieCredits handles empty cast list`() = runTest {
        val request = GetMovieDetailRequest(id = 789)
        val mockResponse = CreditsResponse(
            id = 789,
            cast = emptyList()
        )

        whenever(movieAPI.getMovieCredits(789)).thenReturn(mockResponse)

        val result = repository.getMovieCredits(request)

        assertEquals(789, result.id)
        assertEquals(0, result.cast.size)
    }
}