package com.andriawan.moviedb.ui.detail

import app.cash.turbine.test
import com.andriawan.moviedb.domain.models.Cast
import com.andriawan.moviedb.domain.models.Credits
import com.andriawan.moviedb.domain.models.Genre
import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.utils.result.MovieError
import com.andriawan.moviedb.utils.result.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getMovieDetailUseCase: GetMovieDetailUseCase
    private lateinit var getMovieCreditsUseCase: GetMovieCreditsUseCase
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMovieDetailUseCase = mock()
        getMovieCreditsUseCase = mock()
        viewModel = DetailViewModel(getMovieDetailUseCase, getMovieCreditsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovieDetail emits loading then success`() = runTest {
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

        whenever(getMovieDetailUseCase.invoke(movieId)).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(movieDetail)
            )
        )

        viewModel.getMovieDetail(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val successState = awaitItem()
            assertTrue(successState is ResultState.Success)
            assertEquals(movieDetail, (successState as ResultState.Success).data)

            awaitComplete()
        }
    }

    @Test
    fun `getMovieDetail emits loading then error`() = runTest {
        val movieId = 456
        val error = MovieError.NetworkError("Network error")

        whenever(getMovieDetailUseCase.invoke(movieId)).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Error(error)
            )
        )

        viewModel.getMovieDetail(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            assertEquals(error, (errorState as ResultState.Error).error)

            awaitComplete()
        }
    }

    @Test
    fun `getMovieDetail handles unauthorized error`() = runTest {
        val movieId = 789
        val error = MovieError.Unauthorized

        whenever(getMovieDetailUseCase.invoke(movieId)).thenReturn(
            flowOf(ResultState.Error(error))
        )

        viewModel.getMovieDetail(movieId).test {
            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            assertEquals(MovieError.Unauthorized, (errorState as ResultState.Error).error)

            awaitComplete()
        }
    }

    @Test
    fun `getMovieDetail handles unknown error`() = runTest {
        val movieId = 999
        val error = MovieError.Unknown("Something went wrong")

        whenever(getMovieDetailUseCase.invoke(movieId)).thenReturn(
            flowOf(ResultState.Error(error))
        )

        viewModel.getMovieDetail(movieId).test {
            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val errorMessage = (errorState as ResultState.Error).error.message
            assertEquals("Something went wrong", errorMessage)

            awaitComplete()
        }
    }

    @Test
    fun `getMovieCredits emits loading then success`() = runTest {
        val movieId = 123
        val credits = Credits(
            id = movieId,
            cast = listOf(
                Cast(
                    castId = 1,
                    character = "Hero",
                    id = 100,
                    name = "John Doe",
                    order = 0,
                    originalName = "John Doe",
                    popularity = 50.5,
                    profilePath = "/profile1.jpg"
                ),
                Cast(
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

        whenever(getMovieCreditsUseCase.invoke(movieId)).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(credits)
            )
        )

        viewModel.getMovieCredits(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val successState = awaitItem()
            assertTrue(successState is ResultState.Success)
            val data = (successState as ResultState.Success).data
            assertEquals(credits, data)
            assertEquals(2, data.cast.size)
            assertEquals("John Doe", data.cast[0].name)
            assertEquals("Jane Smith", data.cast[1].name)

            awaitComplete()
        }

    }

    @Test
    fun `getMovieCredits emits loading then error`() = runTest {
        val movieId = 456
        val error = MovieError.NetworkError("Failed to fetch credits")

        whenever(getMovieCreditsUseCase.invoke(movieId)).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Error(error)
            )
        )

        viewModel.getMovieCredits(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            assertEquals(error, (errorState as ResultState.Error).error)

            awaitComplete()
        }

    }

    @Test
    fun `getMovieCredits handles empty cast list`() = runTest {
        val movieId = 789
        val credits = Credits(
            id = movieId,
            cast = emptyList()
        )

        whenever(getMovieCreditsUseCase.invoke(movieId)).thenReturn(
            flowOf(ResultState.Success(credits))
        )

        viewModel.getMovieCredits(movieId).test {
            val successState = awaitItem()
            assertTrue(successState is ResultState.Success)
            val data = (successState as ResultState.Success).data
            assertEquals(0, data.cast.size)

            awaitComplete()
        }
    }

    @Test
    fun `getMovieDetail and getMovieCredits can be called independently`() = runTest {
        val movieId = 100
        val movieDetail = MovieDetail(id = movieId, title = "Test Movie")
        val credits = Credits(id = movieId, cast = emptyList())

        whenever(getMovieDetailUseCase.invoke(movieId)).thenReturn(
            flowOf(ResultState.Success(movieDetail))
        )
        whenever(getMovieCreditsUseCase.invoke(movieId)).thenReturn(
            flowOf(ResultState.Success(credits))
        )

        // Call getMovieDetail
        viewModel.getMovieDetail(movieId).test {
            val state = awaitItem()
            assertTrue(state is ResultState.Success)
            assertEquals(movieDetail, (state as ResultState.Success).data)
            awaitComplete()
        }

        // Call getMovieCredits
        viewModel.getMovieCredits(movieId).test {
            val state = awaitItem()
            assertTrue(state is ResultState.Success)
            assertEquals(credits, (state as ResultState.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `getMovieDetail can be called with different movie IDs`() = runTest {
        val movieId1 = 100
        val movieId2 = 200
        val movieDetail1 = MovieDetail(id = movieId1, title = "Movie 1")
        val movieDetail2 = MovieDetail(id = movieId2, title = "Movie 2")

        whenever(getMovieDetailUseCase.invoke(movieId1)).thenReturn(
            flowOf(ResultState.Success(movieDetail1))
        )
        whenever(getMovieDetailUseCase.invoke(movieId2)).thenReturn(
            flowOf(ResultState.Success(movieDetail2))
        )

        viewModel.getMovieDetail(movieId1).test {
            val state = awaitItem()
            assertEquals("Movie 1", ((state as ResultState.Success).data).title)
            awaitComplete()
        }

        viewModel.getMovieDetail(movieId2).test {
            val state = awaitItem()
            assertEquals("Movie 2", ((state as ResultState.Success).data).title)
            awaitComplete()
        }
    }
}