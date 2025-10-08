package com.andriawan.moviedb.ui.main

import app.cash.turbine.test
import com.andriawan.moviedb.domain.usecases.GetMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getMoviesUseCase: GetMoviesUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMoviesUseCase = mock()
        viewModel = MainViewModel(getMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `query initial value is null`() = runTest {
        viewModel.query.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `searchMovies updates query with non-empty string`() = runTest {
        val searchQuery = "Avengers"

        viewModel.searchMovies(searchQuery)
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals(searchQuery, awaitItem())
        }
    }

    @Test
    fun `searchMovies updates query to null when empty string is provided`() = runTest {
        // First set a query
        viewModel.searchMovies("Avengers")
        advanceUntilIdle()

        // Then search with empty string
        viewModel.searchMovies("")
        advanceUntilIdle()

        viewModel.query.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `searchMovies can be called multiple times`() = runTest {
        viewModel.searchMovies("Query 1")
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals("Query 1", awaitItem())
        }

        viewModel.searchMovies("Query 2")
        advanceUntilIdle()

        viewModel.query.test {
            assertEquals("Query 2", awaitItem())
        }

        viewModel.searchMovies("")
        advanceUntilIdle()

        viewModel.query.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `viewModel is properly initialized`() {
        assertNotNull(viewModel)
        assertNotNull(viewModel.query)
        assertNotNull(viewModel.movies)
    }

    @Test
    fun `searchMovies with whitespace-only string keeps the whitespace`() = runTest {
        viewModel.searchMovies("   ")
        advanceUntilIdle()

        viewModel.query.test {
            // Whitespace-only string is NOT empty, so it's kept as-is
            assertEquals("   ", awaitItem())
        }
    }

    @Test
    fun `query state changes are emitted correctly`() = runTest {
        viewModel.query.test {
            // Initial null
            assertNull(awaitItem())

            viewModel.searchMovies("Test Query")
            advanceUntilIdle()

            assertEquals("Test Query", awaitItem())

            viewModel.searchMovies("")
            advanceUntilIdle()

            assertNull(awaitItem())
        }
    }
}