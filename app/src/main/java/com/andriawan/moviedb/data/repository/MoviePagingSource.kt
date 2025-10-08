package com.andriawan.moviedb.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.domain.models.Movie

class MoviePagingSource(
    private val movieRepository: MovieRepository,
    val query: String?
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val nextPageNumber = params.key ?: 1
            val result = movieRepository.getMovies(
                request = GetMovieListRequest(
                    query = query,
                    page = nextPageNumber
                )
            )

            return LoadResult.Page(
                data = result.results,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (nextPageNumber >= result.totalPages) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}