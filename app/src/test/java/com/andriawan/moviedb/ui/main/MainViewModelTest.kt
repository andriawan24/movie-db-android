package com.andriawan.moviedb.ui.main

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.domain.models.PaginationMovie
import com.andriawan.moviedb.domain.usecases.MovieUseCase
import com.andriawan.moviedb.domain.usecases.MovieUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var movieUseCase: MovieUseCase
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        movieRepository = mock()
        movieUseCase = MovieUseCaseImpl(movieRepository)
        viewModel = MainViewModel(movieUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial query is null`() = runTest {
        viewModel.query.test {
            assertEquals(null, awaitItem())
        }
    }

    @Test
    fun `searchMovies with non-empty query updates query state`() = runTest {
        viewModel.searchMovies("Avengers")
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals("Avengers", awaitItem())
        }
    }

    @Test
    fun `searchMovies with empty query sets null`() = runTest {
        viewModel.searchMovies("Avengers")
        advanceUntilIdle()

        viewModel.searchMovies("")
        advanceUntilIdle()

        viewModel.query.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `searchMovies updates query multiple times`() = runTest {
        viewModel.searchMovies("first")
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals("first", awaitItem())
        }

        viewModel.searchMovies("second")
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals("second", awaitItem())
        }

        viewModel.searchMovies("third")
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals("third", awaitItem())
        }
    }

    @Test
    fun `movies flow handles empty results`() = runTest {
        val paginationMovie = PaginationMovie(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        advanceUntilIdle()
        val snapshot = viewModel.movies.asSnapshot()

        assertEquals(0, snapshot.size)
    }

    @Test
    fun `searchMovies with whitespace only query sets null`() = runTest {
        viewModel.searchMovies("   ")
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals("   ", awaitItem())
        }
    }
}