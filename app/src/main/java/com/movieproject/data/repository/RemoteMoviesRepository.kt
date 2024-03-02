package com.movieproject.data.repository

import com.movieproject.data.MovieService
import com.movieproject.data.model.MovieInfoModel
import com.movieproject.data.model.MovieModel
import com.movieproject.ui.MovieInfo
import com.movieproject.ui.Movie

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"

class RemoteMoviesRepository(
    private val movieService: MovieService
) : MoviesRepository {

    override suspend fun getMoviesData(): RepositoryResult<List<Movie>> {
        return runCatching {
            movieService.getMovies()
        }.fold(
            onSuccess = {
                RepositoryResult.Success(it.result.map {
                    it.mapToMovie()
                })
            },
            onFailure = {
                RepositoryResult.Error(it)
            }
        )
    }

    override suspend fun getMovieInfoData(movieId: Int): RepositoryResult<MovieInfo> {
        return runCatching {
            movieService.getMovieInfo(movieId)
        }.fold(
            onSuccess = {
                RepositoryResult.Success(
                    it.mapToMovieInfo()
                )
            },
            onFailure = {
                RepositoryResult.Error(it)
            }
        )
    }
}

fun MovieModel.mapToMovie(): Movie {
    return Movie(id, title, BASE_IMAGE_URL + url, releaseDate)
}

fun MovieInfoModel.mapToMovieInfo(): MovieInfo {
    return MovieInfo(
        title,
        BASE_IMAGE_URL + url,
        releaseDate,
        budget,
        revenue,
        duration,
        rating,
        totalVote,
        genres.map { it.genre },
        overview
    )
}