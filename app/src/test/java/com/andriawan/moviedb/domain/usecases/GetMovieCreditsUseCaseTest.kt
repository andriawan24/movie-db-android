package com.andriawan.moviedb.domain.usecases

import app.cash.turbine.test
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.data.requests.GetMovieDetailRequest
import com.andriawan.moviedb.domain.models.Cast
import com.andriawan.moviedb.domain.models.Credits
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

class GetMovieCreditsUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMovieCreditsUseCase

    @Before
    fun setUp() {
        movieRepository = mock()
        useCase = GetMovieCreditsUseCase(movieRepository)
    }

    @Test
    fun `invoke emits loading then success when repository returns data`() = runTest {
        val movieId = 123
        val credits = Credits(
            id = movieId,
            cast = listOf(
                Cast(
                    castId = 1,
                    character = "Tony Stark / Iron Man",
                    id = 3223,
                    name = "Robert Downey Jr.",
                    order = 0,
                    originalName = "Robert Downey Jr.",
                    popularity = 50.5,
                    profilePath = "/profile.jpg"
                ),
                Cast(
                    castId = 2,
                    character = "Steve Rogers / Captain America",
                    id = 1896,
                    name = "Chris Evans",
                    order = 1,
                    originalName = "Chris Evans",
                    popularity = 45.3,
                    profilePath = "/profile2.jpg"
                )
            )
        )

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenReturn(credits)

        useCase.invoke(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val successState = awaitItem()
            assertTrue(successState is ResultState.Success)
            val data = (successState as ResultState.Success).data
            assertEquals(credits, data)
            assertEquals(movieId, data.id)
            assertEquals(2, data.cast.size)
            assertEquals("Robert Downey Jr.", data.cast[0].name)
            assertEquals("Chris Evans", data.cast[1].name)

            awaitComplete()
        }

        verify(movieRepository).getMovieCredits(GetMovieDetailRequest(movieId))
    }

    @Test
    fun `invoke emits loading then error when repository throws exception`() = runTest {
        val movieId = 456
        val exception = RuntimeException("Failed to fetch credits")

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is ResultState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.Unknown)
            assertEquals("Failed to fetch credits", error.message)

            awaitComplete()
        }

        verify(movieRepository).getMovieCredits(GetMovieDetailRequest(movieId))
    }

    @Test
    fun `invoke handles network error message as NetworkError`() = runTest {
        val movieId = 789
        val exception = RuntimeException("network timeout occurred")

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            awaitItem()

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.NetworkError)
            assertEquals("network timeout occurred", error.message)

            awaitComplete()
        }
    }

    @Test
    fun `invoke handles 401 error as Unauthorized`() = runTest {
        val movieId = 111
        val exception = RuntimeException("401 Unauthorized access")

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            awaitItem()

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.Unauthorized)
            assertEquals("Unauthorized access", error.message)

            awaitComplete()
        }
    }

    @Test
    fun `invoke handles 404 error as Unknown with resource not found message`() = runTest {
        val movieId = 999
        val exception = RuntimeException("404 Movie credits not found")

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenThrow(exception)

        useCase.invoke(movieId).test {
            awaitItem()

            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            val error = (errorState as ResultState.Error).error
            assertTrue(error is MovieError.Unknown)
            assertEquals("Resource not found", error.message)

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns credits with empty cast list`() = runTest {
        val movieId = 555
        val credits = Credits(
            id = movieId,
            cast = emptyList()
        )

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenReturn(credits)

        useCase.invoke(movieId).test {
            awaitItem()

            val successState = awaitItem()
            val data = (successState as ResultState.Success).data

            assertEquals(movieId, data.id)
            assertTrue(data.cast.isEmpty())

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns credits with single cast member`() = runTest {
        val movieId = 666
        val credits = Credits(
            id = movieId,
            cast = listOf(
                Cast(
                    castId = 1,
                    character = "Solo Character",
                    id = 100,
                    name = "Solo Actor",
                    order = 0,
                    originalName = "Solo Actor",
                    popularity = 80.0,
                    profilePath = "/solo.jpg"
                )
            )
        )

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenReturn(credits)

        useCase.invoke(movieId).test {
            awaitItem()

            val successState = awaitItem()
            val data = (successState as ResultState.Success).data

            assertEquals(movieId, data.id)
            assertEquals(1, data.cast.size)
            assertEquals("Solo Actor", data.cast[0].name)
            assertEquals("Solo Character", data.cast[0].character)

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns credits with large cast list`() = runTest {
        val movieId = 777
        val largeCast = List(50) { index ->
            Cast(
                castId = index,
                character = "Character $index",
                id = 1000 + index,
                name = "Actor $index",
                order = index,
                originalName = "Actor $index",
                popularity = 10.0 + index,
                profilePath = "/actor$index.jpg"
            )
        }
        val credits = Credits(
            id = movieId,
            cast = largeCast
        )

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenReturn(credits)

        useCase.invoke(movieId).test {
            awaitItem()

            val successState = awaitItem()
            val data = (successState as ResultState.Success).data

            assertEquals(movieId, data.id)
            assertEquals(50, data.cast.size)
            assertEquals("Actor 0", data.cast[0].name)
            assertEquals("Actor 49", data.cast[49].name)

            awaitComplete()
        }
    }

    @Test
    fun `invoke verifies cast members are in correct order`() = runTest {
        val movieId = 888
        val credits = Credits(
            id = movieId,
            cast = listOf(
                Cast(castId = 1, character = "Lead", id = 1, name = "First Actor", order = 0),
                Cast(
                    castId = 2,
                    character = "Supporting",
                    id = 2,
                    name = "Second Actor",
                    order = 1
                ),
                Cast(castId = 3, character = "Minor", id = 3, name = "Third Actor", order = 2)
            )
        )

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId)))
            .thenReturn(credits)

        useCase.invoke(movieId).test {
            awaitItem()

            val successState = awaitItem()
            val data = (successState as ResultState.Success).data

            assertEquals(0, data.cast[0].order)
            assertEquals(1, data.cast[1].order)
            assertEquals(2, data.cast[2].order)
            assertEquals("First Actor", data.cast[0].name)
            assertEquals("Second Actor", data.cast[1].name)
            assertEquals("Third Actor", data.cast[2].name)

            awaitComplete()
        }
    }

    @Test
    fun `invoke can be called multiple times with different movie IDs`() = runTest {
        val movieId1 = 100
        val movieId2 = 200

        val credits1 = Credits(
            id = movieId1,
            cast = listOf(Cast(id = 1, name = "Actor 1", character = "Character 1"))
        )
        val credits2 = Credits(
            id = movieId2,
            cast = listOf(Cast(id = 2, name = "Actor 2", character = "Character 2"))
        )

        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId1)))
            .thenReturn(credits1)
        whenever(movieRepository.getMovieCredits(GetMovieDetailRequest(movieId2)))
            .thenReturn(credits2)

        useCase.invoke(movieId1).test {
            awaitItem()
            val result1 = (awaitItem() as ResultState.Success).data
            assertEquals(movieId1, result1.id)
            assertEquals("Actor 1", result1.cast[0].name)
            awaitComplete()
        }

        useCase.invoke(movieId2).test {
            awaitItem()
            val result2 = (awaitItem() as ResultState.Success).data
            assertEquals(movieId2, result2.id)
            assertEquals("Actor 2", result2.cast[0].name)
            awaitComplete()
        }

        verify(movieRepository).getMovieCredits(GetMovieDetailRequest(movieId1))
        verify(movieRepository).getMovieCredits(GetMovieDetailRequest(movieId2))
    }
}