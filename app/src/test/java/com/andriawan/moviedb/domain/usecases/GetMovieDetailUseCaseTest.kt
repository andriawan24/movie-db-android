package com.andriawan.moviedb.domain.usecases

import app.cash.turbine.test
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.data.requests.GetMovieDetailRequest
import com.andriawan.moviedb.domain.models.Genre
import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.utils.result.MovieError
import com.andriawan.moviedb.utils.result.ResultState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetMovieDetailUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMovieDetailUseCase

    @Before
    fun setUp() {
        movieRepository = mock()
        useCase = GetMovieDetailUseCase(movieRepository)
    }

    @Test
    fun `invoke emits loading then success when repository returns data`() = runTest {
        val movieId = 123
        val movieDetail = MovieDetail(
            id = movieId,
            title = "Test Movie",
            adult = false,
            backdropPath = "/backdrop.jpg",
            budget = 200000000,
            genres = listOf(
                Genre(id = 28, name = "Action"),
                Genre(id = 12, name = "Adventure")
            ),
            homepage = "https://example.com",
            imdbId = "tt1234567",
            originalLanguage = "en",
            originalTitle = "Test Movie",
            overview = "Test overview",
            popularity = 150.5,
            posterPath = "/poster.jpg",
            releaseDate = "2024-01-01",
            revenue = 500000000,
            runtime = 120,
            status = "Released",
            tagline = "An epic test",
            video = false,
            voteAverage = 8.5,
            voteCount = 5000
        )

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId)))
            .thenReturn(movieDetail)

        useCase.invoke(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val successState = awaitItem()
            assertTrue(successState is ResultState.Success)
            assertEquals(movieDetail, (successState as ResultState.Success).data)
            assertEquals("Test Movie", successState.data.title)
            assertEquals(movieId, successState.data.id)

            awaitComplete()
        }

        verify(movieRepository).getMovieDetail(GetMovieDetailRequest(movieId))
    }

    @Test
    fun `invoke emits loading then error when repository throws exception`() = runTest {
        val movieId = 456
        val exception = RuntimeException("Something went wrong")

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.Unknown)
            assertEquals("Something went wrong", error.message)

            awaitComplete()
        }

        verify(movieRepository).getMovieDetail(GetMovieDetailRequest(movieId))
    }

    @Test
    fun `invoke handles network error message as NetworkError`() = runTest {
        val movieId = 789
        val exception = RuntimeException("network connection failed")

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            awaitItem() // Loading

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.NetworkError)
            assertEquals("network connection failed", error.message)

            awaitComplete()
        }
    }

    @Test
    fun `invoke handles 404 error as Unknown with appropriate message`() = runTest {
        val movieId = 999
        val exception = RuntimeException("404 Not Found")

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            awaitItem() // Loading

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.Unknown)
            assertEquals("Resource not found", error.message)

            awaitComplete()
        }
    }

    @Test
    fun `invoke handles 401 error as Unauthorized`() = runTest {
        val movieId = 111
        val exception = RuntimeException("401 Unauthorized")

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            awaitItem() // Loading

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.Unauthorized)
            assertEquals("Unauthorized access", error.message)

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns movie detail with all fields populated`() = runTest {
        val movieId = 555
        val movieDetail = MovieDetail(
            id = movieId,
            title = "Full Detail Movie",
            adult = true,
            backdropPath = "/backdrop.jpg",
            budget = 100000000,
            genres = listOf(Genre(id = 28, name = "Action")),
            homepage = "https://movie.com",
            imdbId = "tt9999999",
            originalLanguage = "fr",
            originalTitle = "Le Film",
            overview = "A comprehensive test",
            popularity = 200.0,
            posterPath = "/poster.jpg",
            releaseDate = "2023-06-15",
            revenue = 300000000,
            runtime = 150,
            status = "Released",
            tagline = "Test all fields",
            video = true,
            voteAverage = 7.8,
            voteCount = 10000
        )

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId)))
            .thenReturn(movieDetail)

        useCase.invoke(movieId).test {
            awaitItem() // Loading

            val successState = awaitItem()
            val data = (successState as ResultState.Success).data

            assertEquals(movieId, data.id)
            assertEquals("Full Detail Movie", data.title)
            assertTrue(data.adult)
            assertEquals("/backdrop.jpg", data.backdropPath)
            assertEquals(100000000, data.budget)
            assertEquals(1, data.genres.size)
            assertEquals("https://movie.com", data.homepage)
            assertEquals("tt9999999", data.imdbId)
            assertEquals("fr", data.originalLanguage)
            assertEquals("Le Film", data.originalTitle)
            assertEquals("A comprehensive test", data.overview)
            assertEquals(200.0, data.popularity, 0.01)
            assertEquals("/poster.jpg", data.posterPath)
            assertEquals("2023-06-15", data.releaseDate)
            assertEquals(300000000, data.revenue)
            assertEquals(150, data.runtime)
            assertEquals("Released", data.status)
            assertEquals("Test all fields", data.tagline)
            assertTrue(data.video)
            assertEquals(7.8, data.voteAverage, 0.01)
            assertEquals(10000, data.voteCount)

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns movie detail with minimal data`() = runTest {
        val movieId = 666
        val movieDetail = MovieDetail(
            id = movieId,
            title = "Minimal Movie"
        )

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId)))
            .thenReturn(movieDetail)

        useCase.invoke(movieId).test {
            awaitItem() // Loading

            val successState = awaitItem()
            val data = (successState as ResultState.Success).data

            assertEquals(movieId, data.id)
            assertEquals("Minimal Movie", data.title)
            assertEquals(0, data.budget)
            assertEquals(0, data.revenue)
            assertEquals(0, data.runtime)

            awaitComplete()
        }
    }

    @Test
    fun `invoke can be called multiple times with different IDs`() = runTest {
        val movieId1 = 100
        val movieId2 = 200

        val movieDetail1 = MovieDetail(id = movieId1, title = "Movie 1")
        val movieDetail2 = MovieDetail(id = movieId2, title = "Movie 2")

        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId1)))
            .thenReturn(movieDetail1)
        whenever(movieRepository.getMovieDetail(GetMovieDetailRequest(movieId2)))
            .thenReturn(movieDetail2)

        useCase.invoke(movieId1).test {
            awaitItem() // Loading
            val result1 = (awaitItem() as ResultState.Success).data
            assertEquals("Movie 1", result1.title)
            awaitComplete()
        }

        useCase.invoke(movieId2).test {
            awaitItem() // Loading
            val result2 = (awaitItem() as ResultState.Success).data
            assertEquals("Movie 2", result2.title)
            awaitComplete()
        }

        verify(movieRepository).getMovieDetail(GetMovieDetailRequest(movieId1))
        verify(movieRepository).getMovieDetail(GetMovieDetailRequest(movieId2))
    }
}