package com.andriawan.moviedb.domain.usecases

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.domain.models.Movie
import com.andriawan.moviedb.domain.models.PaginationMovie
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MoviePagingSourceTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var pagingSource: MoviePagingSource

    @Before
    fun setUp() {
        movieRepository = mock()
    }

    @Test
    fun `load returns page when successful`() = runTest {
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

        pagingSource = MoviePagingSource(movieRepository, query = null)
        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(3, page.data.size)
        assertEquals(movies, page.data)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load with null key starts at page 1`() = runTest {
        val paginationMovie = PaginationMovie(
            page = 1,
            results = listOf(Movie(id = 1, title = "Movie 1")),
            totalPages = 5,
            totalResults = 100
        )

        pagingSource = MoviePagingSource(movieRepository, query = null)
        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        verify(movieRepository).getMovies(GetMovieListRequest(query = null, page = 1))
    }

    @Test
    fun `load with key uses provided page number`() = runTest {
        val paginationMovie = PaginationMovie(
            page = 3,
            results = listOf(Movie(id = 1, title = "Movie 1")),
            totalPages = 5,
            totalResults = 100
        )

        pagingSource = MoviePagingSource(movieRepository, query = null)
        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 3,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        verify(movieRepository).getMovies(GetMovieListRequest(query = null, page = 3))
    }

    @Test
    fun `load with query passes query to repository`() = runTest {
        val paginationMovie = PaginationMovie(
            page = 1,
            results = listOf(Movie(id = 1, title = "Searched Movie")),
            totalPages = 2,
            totalResults = 30
        )

        pagingSource = MoviePagingSource(movieRepository, query = "test")
        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        verify(movieRepository).getMovies(GetMovieListRequest(query = "test", page = 1))
    }

    @Test
    fun `load returns null nextKey when results are empty`() = runTest {
        val paginationMovie = PaginationMovie(
            page = 5,
            results = emptyList(),
            totalPages = 5,
            totalResults = 100
        )

        pagingSource = MoviePagingSource(movieRepository, query = null)
        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 5,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertNull(page.nextKey)
    }

    @Test
    fun `load returns error when repository throws exception`() = runTest {
        val exception = RuntimeException("Network error")
        pagingSource = MoviePagingSource(movieRepository, query = null)
        whenever(movieRepository.getMovies(any())).thenThrow(exception)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals(exception, error.throwable)
    }

    @Test
    fun `load calculates nextKey correctly`() = runTest {
        val paginationMovie = PaginationMovie(
            page = 3,
            results = listOf(Movie(id = 1, title = "Movie 1")),
            totalPages = 10,
            totalResults = 200
        )

        pagingSource = MoviePagingSource(movieRepository, query = null)
        whenever(movieRepository.getMovies(any())).thenReturn(paginationMovie)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 3,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(4, page.nextKey)
    }

    @Test
    fun `getRefreshKey returns null for empty paging state`() = runTest {
        pagingSource = MoviePagingSource(movieRepository, query = null)
        val pagingState = PagingState<Int, Movie>(
            pages = emptyList(),
            anchorPosition = 10,
            config = androidx.paging.PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(pagingState)

        assertNull(refreshKey)
    }
}