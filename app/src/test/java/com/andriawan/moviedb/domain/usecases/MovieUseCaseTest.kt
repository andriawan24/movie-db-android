package com.andriawan.moviedb.domain.usecases

import androidx.paging.testing.asSnapshot
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.domain.models.Movie
import com.andriawan.moviedb.domain.models.PaginationMovie
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import timber.log.Timber

class MovieUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var movieUseCase: MovieUseCase

    @Before
    fun setUp() {
        movieRepository = mock()
        movieUseCase = MovieUseCase(movieRepository)
    }

    @Test
    fun `getMovies returns Flow of PagingData`() = runTest {
        val movies = listOf(
            Movie(id = 1, title = "Movie 1"),
            Movie(id = 2, title = "Movie 2"),
            Movie(id = 3, title = "Movie 3")
        )
        val paginationMovie = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 5,
            totalResults = 100
        )

        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val flow = movieUseCase.getMovies(query = null)

        assertNotNull(flow)

        val snapshot: List<Movie> = flow.asSnapshot()

        assertEquals(15, snapshot.size)
        assertEquals("Movie 1", snapshot[0].title)
        assertEquals("Movie 2", snapshot[1].title)
        assertEquals("Movie 3", snapshot[2].title)
    }

    @Test
    fun `getMovies with query passes query to paging source`() = runTest {
        val movies = listOf(Movie(id = 1, title = "Searched Movie"))
        val paginationMovie = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 1,
            totalResults = 1
        )

        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val flow = movieUseCase.getMovies(query = "test")

        val snapshot = flow.asSnapshot()
        verify(movieRepository).getMovies(GetMovieListRequest(query = "test", page = 1))
        assertEquals(1, snapshot.size)
        assertEquals("Searched Movie", snapshot[0].title)
    }

    @Test
    fun `getMovies with null query searches all movies`() = runTest {
        val movies = listOf(
            Movie(id = 1, title = "All Movie 1"),
            Movie(id = 2, title = "All Movie 2")
        )
        val paginationMovie = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 5,
            totalResults = 100
        )

        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val flow = movieUseCase.getMovies(query = null)

        val snapshot = flow.asSnapshot()
        verify(movieRepository).getMovies(GetMovieListRequest(query = null, page = 1))
        assertEquals(10, snapshot.size)
    }

    @Test
    fun `getMovies loads multiple pages`() = runTest {
        val moviesPage1 = listOf(
            Movie(id = 1, title = "Movie 1"),
            Movie(id = 2, title = "Movie 2")
        )
        val paginationMoviePage1 = PaginationMovie(
            page = 1,
            results = moviesPage1,
            totalPages = 2,
            totalResults = 4
        )

        val moviesPage2 = listOf(
            Movie(id = 3, title = "Movie 3"),
            Movie(id = 4, title = "Movie 4")
        )
        val paginationMoviePage2 = PaginationMovie(
            page = 2,
            results = moviesPage2,
            totalPages = 2,
            totalResults = 4
        )

        whenever(movieRepository.getMovies(GetMovieListRequest(query = null, page = 1)))
            .thenReturn(paginationMoviePage1)
        whenever(movieRepository.getMovies(GetMovieListRequest(query = null, page = 2)))
            .thenReturn(paginationMoviePage2)

        val flow = movieUseCase.getMovies(query = null)

        val snapshot = flow.asSnapshot()
        assertEquals(4, snapshot.size)
        assertEquals("Movie 1", snapshot[0].title)
        assertEquals("Movie 2", snapshot[1].title)
        assertEquals("Movie 3", snapshot[2].title)
        assertEquals("Movie 4", snapshot[3].title)
    }

    @Test
    fun `getMovies returns empty list when no results`() = runTest {
        val paginationMovie = PaginationMovie(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val flow = movieUseCase.getMovies(query = null)

        val snapshot = flow.asSnapshot()
        assertEquals(0, snapshot.size)
    }

    @Test
    fun `getMovies uses correct page size`() = runTest {
        val movies = List(20) { index -> Movie(id = index, title = "Movie $index") }
        val paginationMovie = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 10,
            totalResults = 200
        )

        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val flow = movieUseCase.getMovies(query = null)

        val snapshot = flow.asSnapshot()
        assertEquals(40, snapshot.size)
    }
}