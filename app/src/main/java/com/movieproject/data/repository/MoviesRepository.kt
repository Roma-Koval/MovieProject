package com.movieproject.data.repository

import com.movieproject.ui.MovieInfo
import com.movieproject.ui.Movie

interface MoviesRepository {
    suspend fun getMoviesData(): RepositoryResult<List<Movie>>

    suspend fun getMovieInfoData(movieId: Int): RepositoryResult<MovieInfo>
}