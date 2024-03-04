package com.movieproject.data.repository

import androidx.paging.PagingData
import com.movieproject.ui.Movie
import com.movieproject.ui.MovieInfo
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
//    suspend fun getMoviesData(): RepositoryResult<List<Movie>>

    suspend fun getMovieInfoData(movieId: Int): RepositoryResult<MovieInfo>

    suspend fun getPagedMovies(): Flow<PagingData<Movie>>
}