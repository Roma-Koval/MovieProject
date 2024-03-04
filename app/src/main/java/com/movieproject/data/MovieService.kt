package com.movieproject.data

import com.movieproject.data.model.DataResult
import com.movieproject.data.model.MovieInfoModel
import com.movieproject.data.model.MovieModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("trending/movie/day?api_key=7654bf4e6c60a89f3794c13ee8e8223d")
    suspend fun getMovies(@Query("page") page: Int): DataResult<MovieModel>

    @GET("movie/{id}?api_key=7654bf4e6c60a89f3794c13ee8e8223d")
    suspend fun getMovieInfo(@Path("id") id: Int): MovieInfoModel
}