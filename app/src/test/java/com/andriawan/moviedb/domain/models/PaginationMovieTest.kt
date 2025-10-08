package com.andriawan.moviedb.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PaginationMovieTest {

    @Test
    fun `PaginationMovie creation with all parameters`() {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                adult = false,
                genreIds = emptyList(),
                popularity = 0.0,
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            ),
            Movie(
                id = 2,
                title = "Movie 2",
                adult = false,
                genreIds = emptyList(),
                popularity = 0.0,
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            )
        )
        val pagination = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 10,
            totalResults = 200
        )

        assertEquals(1, pagination.page)
        assertEquals(movies, pagination.results)
        assertEquals(10, pagination.totalPages)
        assertEquals(200, pagination.totalResults)
    }

    @Test
    fun `PaginationMovie with default values`() {
        val pagination = PaginationMovie()

        assertEquals(0, pagination.page)
        assertEquals(emptyList<Movie>(), pagination.results)
        assertEquals(0, pagination.totalPages)
        assertEquals(0, pagination.totalResults)
    }

    @Test
    fun `PaginationMovie equality with same values`() {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie",
                adult = false,
                genreIds = emptyList(),
                popularity = 0.0,
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            )
        )
        val pagination1 = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 5,
            totalResults = 100
        )
        val pagination2 = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 5,
            totalResults = 100
        )

        assertEquals(pagination1, pagination2)
        assertEquals(pagination1.hashCode(), pagination2.hashCode())
    }

    @Test
    fun `PaginationMovie inequality with different values`() {
        val pagination1 = PaginationMovie(page = 1)
        val pagination2 = PaginationMovie(page = 2)

        assertNotEquals(pagination1, pagination2)
    }

    @Test
    fun `PaginationMovie copy function works correctly`() {
        val original = PaginationMovie(page = 1, totalPages = 10)
        val copied = original.copy(page = 2)

        assertEquals(2, copied.page)
        assertEquals(10, copied.totalPages)
        assertNotEquals(original, copied)
    }

    @Test
    fun `PaginationMovie handles first page`() {
        val pagination = PaginationMovie(
            page = 1,
            results = listOf(
                Movie(
                    id = 1,
                    title = "Movie",
                    adult = false,
                    genreIds = emptyList(),
                    popularity = 0.0,
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            ),
            totalPages = 10,
            totalResults = 200
        )

        assertEquals(1, pagination.page)
        assertTrue(pagination.page == 1)
    }

    @Test
    fun `PaginationMovie handles last page`() {
        val pagination = PaginationMovie(
            page = 10,
            results = listOf(
                Movie(
                    id = 1,
                    title = "Movie",
                    adult = false,
                    genreIds = emptyList(),
                    popularity = 0.0,
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            ),
            totalPages = 10,
            totalResults = 200
        )

        assertEquals(10, pagination.page)
        assertEquals(pagination.page, pagination.totalPages)
    }

    @Test
    fun `PaginationMovie handles empty results`() {
        val pagination = PaginationMovie(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        assertTrue(pagination.results.isEmpty())
        assertEquals(0, pagination.totalPages)
        assertEquals(0, pagination.totalResults)
    }

    @Test
    fun `PaginationMovie handles multiple movies per page`() {
        val movies = List(20) { index ->
            Movie(
                id = index,
                title = "Movie $index",
                adult = false,
                genreIds = emptyList(),
                popularity = 0.0,
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            )
        }
        val pagination = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 5,
            totalResults = 100
        )

        assertEquals(20, pagination.results.size)
        assertEquals(100, pagination.totalResults)
    }

    @Test
    fun `PaginationMovie calculates pagination correctly`() {
        val pagination = PaginationMovie(
            page = 3,
            results = listOf(
                Movie(
                    id = 1,
                    title = "Movie",
                    adult = false,
                    genreIds = emptyList(),
                    popularity = 0.0,
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            ),
            totalPages = 10,
            totalResults = 200
        )

        // Assuming 20 items per page
        assertEquals(3, pagination.page)
        assertEquals(200, pagination.totalResults)
        assertEquals(10, pagination.totalPages)
    }

    @Test
    fun `PaginationMovie handles single page results`() {
        val pagination = PaginationMovie(
            page = 1,
            results = listOf(
                Movie(
                    id = 1,
                    title = "Movie 1",
                    adult = false,
                    genreIds = emptyList(),
                    popularity = 0.0,
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                ),
                Movie(
                    id = 2,
                    title = "Movie 2",
                    adult = false,
                    genreIds = emptyList(),
                    popularity = 0.0,
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            ),
            totalPages = 1,
            totalResults = 2
        )

        assertEquals(1, pagination.page)
        assertEquals(1, pagination.totalPages)
        assertEquals(2, pagination.totalResults)
        assertEquals(2, pagination.results.size)
    }

    @Test
    fun `PaginationMovie results size matches expected count`() {
        val movies = List(15) { index ->
            Movie(
                id = index,
                title = "Movie $index",
                adult = false,
                genreIds = emptyList(),
                popularity = 0.0,
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            )
        }
        val pagination = PaginationMovie(
            page = 1,
            results = movies,
            totalPages = 2,
            totalResults = 25
        )

        assertEquals(15, pagination.results.size)
        assertEquals(25, pagination.totalResults)
    }
}