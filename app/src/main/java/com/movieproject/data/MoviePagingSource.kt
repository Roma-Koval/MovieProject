package com.movieproject.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.movieproject.data.model.MovieModel
import com.movieproject.data.repository.BASE_IMAGE_URL
import com.movieproject.ui.Movie

const val TOTAL_PAGE = 500

class MoviePagingSource(
    private val movieService: MovieService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1

            val movieList = movieService.getMovies(currentPage).result.map {
                it.mapToMovie()
            }
            LoadResult.Page(
                data = movieList,
                prevKey = null,
                nextKey = if (currentPage == TOTAL_PAGE) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}

fun MovieModel.mapToMovie(): Movie {
    return Movie(id, title, BASE_IMAGE_URL + url, releaseDate)
}