package com.andriawan.moviedb.domain.usecases

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andriawan.moviedb.data.repository.MovieRepository
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

            val nextPage = if (result.results.isEmpty()) null else result.page + 1

            return LoadResult.Page(
                data = result.results,
                prevKey = null,
                nextKey = nextPage
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