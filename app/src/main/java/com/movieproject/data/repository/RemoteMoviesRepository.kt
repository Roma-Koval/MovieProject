package com.movieproject.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.movieproject.data.MoviePagingSource
import com.movieproject.data.MovieService
import com.movieproject.data.model.MovieInfoModel
import com.movieproject.ui.MovieInfo
import com.movieproject.ui.Movie
import kotlinx.coroutines.flow.Flow

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
const val PAGE_SIZE = 20

class RemoteMoviesRepository(
    private val movieService: MovieService
) : MoviesRepository {

    override suspend fun getPagedMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
            ),
            pagingSourceFactory = { MoviePagingSource(movieService) }
        ).flow
    }

//    override suspend fun getMoviesData(): RepositoryResult<List<Movie>> {
//        return runCatching {
//            movieService.getMovies(2)
//        }.fold(
//            onSuccess = {
//                RepositoryResult.Success(it.result.map {
//                    it.mapToMovie()
//                })
//            },
//            onFailure = {
//                RepositoryResult.Error(it)
//            }
//        )
//    }

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