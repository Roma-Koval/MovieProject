package com.movieproject.di

import com.movieproject.data.MovieService
import com.movieproject.data.repository.MoviesRepository
import com.movieproject.data.repository.RemoteMoviesRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideRemoteRepository(movieService: MovieService): MoviesRepository {
        return RemoteMoviesRepository(movieService)
    }
}