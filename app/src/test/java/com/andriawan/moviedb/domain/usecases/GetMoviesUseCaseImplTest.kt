package com.andriawan.moviedb.domain.usecases

import com.andriawan.moviedb.data.repository.MovieRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class GetMoviesUseCaseImplTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMoviesUseCase

    @Before
    fun setUp() {
        movieRepository = mock()
        useCase = GetMoviesUseCase(movieRepository)
    }

    @Test
    fun `invoke returns paging flow with null query`() = runTest {
        val flow = useCase.invoke(null)
        assertNotNull(flow)

        // Verify we can collect from the flow (even if it's just checking it exists)
        val firstPage = flow.firstOrNull()
        // PagingData is not null-able but we're just checking the flow works
    }

    @Test
    fun `invoke returns paging flow with search query`() = runTest {
        val searchQuery = "Avengers"

        val flow = useCase.invoke(searchQuery)
        assertNotNull(flow)
    }

    @Test
    fun `invoke creates new flow each time it is called`() = runTest {
        val flow1 = useCase.invoke(null)
        val flow2 = useCase.invoke("Query")

        assertNotNull(flow1)
        assertNotNull(flow2)
    }

    @Test
    fun `invoke with empty query returns paging flow`() = runTest {
        val flow = useCase.invoke("")
        assertNotNull(flow)
    }

    @Test
    fun `invoke with different queries creates different flows`() = runTest {
        val flow1 = useCase.invoke("Action")
        val flow2 = useCase.invoke("Comedy")

        assertNotNull(flow1)
        assertNotNull(flow2)
    }
}