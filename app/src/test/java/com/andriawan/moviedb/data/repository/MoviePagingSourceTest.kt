package com.andriawan.moviedb.data.repository

import androidx.paging.PagingSource
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

    private fun createMovie(id: Int, title: String) = Movie(
        id = id,
        title = title,
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = listOf(28),
        originalLanguage = "en",
        originalTitle = title,
        overview = "Overview",
        popularity = 10.0,
        posterPath = "/poster.jpg",
        releaseDate = "2024-01-01",
        video = false,
        voteAverage = 8.0,
        voteCount = 100
    )

    @Test
    fun `paging returns success with query`() = runTest {
        val query = "test"
        pagingSource = MoviePagingSource(movieRepository, query)

        val mockMovies = listOf(
            createMovie(1, "Movie 1"),
            createMovie(2, "Movie 2"),
            createMovie(3, "Movie 3")
        )

        val mockPagination = PaginationMovie(
            page = 1,
            results = mockMovies,
            totalPages = 5,
            totalResults = 100
        )

        whenever(movieRepository.getMovies(any())).thenReturn(mockPagination)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(mockMovies, pageResult.data)
        assertNull(pageResult.prevKey)
        assertEquals(2, pageResult.nextKey)

        verify(movieRepository).getMovies(
            GetMovieListRequest(query = query, page = 1)
        )
    }

    @Test
    fun `paging returns success when query is null`() = runTest {
        pagingSource = MoviePagingSource(movieRepository, null)

        val mockMovies = listOf(
            createMovie(1, "Movie 1"),
            createMovie(2, "Movie 2")
        )

        val mockPagination = PaginationMovie(
            page = 1,
            results = mockMovies,
            totalPages = 3,
            totalResults = 50
        )

        whenever(movieRepository.getMovies(any())).thenReturn(mockPagination)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(mockMovies, pageResult.data)
        assertNull(pageResult.prevKey)
        assertEquals(2, pageResult.nextKey)

        verify(movieRepository).getMovies(GetMovieListRequest(query = null, page = 1))
    }

    @Test
    fun `paging returns success on the first page`() = runTest {
        pagingSource = MoviePagingSource(movieRepository, null)

        val mockMovies = listOf(createMovie(1, "First Movie"))

        val mockPagination = PaginationMovie(
            page = 1,
            results = mockMovies,
            totalPages = 5,
            totalResults = 100
        )

        whenever(movieRepository.getMovies(any())).thenReturn(mockPagination)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertNull(pageResult.prevKey)
        assertEquals(2, pageResult.nextKey)
    }

    @Test
    fun `paging returns success on the middle page`() = runTest {
        pagingSource = MoviePagingSource(movieRepository, null)

        val mockMovies = listOf(
            createMovie(10, "Movie 10"),
            createMovie(11, "Movie 11")
        )

        val mockPagination = PaginationMovie(
            page = 3,
            results = mockMovies,
            totalPages = 5,
            totalResults = 100
        )

        whenever(movieRepository.getMovies(any())).thenReturn(mockPagination)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 3,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(mockMovies, pageResult.data)
        assertEquals(2, pageResult.prevKey)
        assertEquals(4, pageResult.nextKey)
    }

    @Test
    fun `paging returns success on the last page`() = runTest {
        pagingSource = MoviePagingSource(movieRepository, null)

        val mockMovies = listOf(createMovie(100, "Last Movie"))

        val mockPagination = PaginationMovie(
            page = 5,
            results = mockMovies,
            totalPages = 5,
            totalResults = 100
        )

        whenever(movieRepository.getMovies(any())).thenReturn(mockPagination)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 5,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(mockMovies, pageResult.data)
        assertEquals(4, pageResult.prevKey)
        assertNull(pageResult.nextKey)
    }

    @Test
    fun `paging returns success on empty state`() = runTest {
        pagingSource = MoviePagingSource(movieRepository, null)

        val mockPagination = PaginationMovie(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        whenever(movieRepository.getMovies(any())).thenReturn(mockPagination)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertTrue(pageResult.data.isEmpty())
        assertNull(pageResult.prevKey)
        assertNull(pageResult.nextKey)
    }

    @Test
    fun `paging returns error on when repository throw exception`() = runTest {
        pagingSource = MoviePagingSource(movieRepository, null)

        val exception = RuntimeException("Network error")
        whenever(movieRepository.getMovies(any())).thenThrow(exception)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertEquals(exception, errorResult.throwable)
    }
}