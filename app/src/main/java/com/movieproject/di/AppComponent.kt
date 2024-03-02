package com.movieproject.di

import com.movieproject.ui.detail.DetailFragment
import com.movieproject.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(detailFragment: DetailFragment)
}