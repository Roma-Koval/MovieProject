package com.movieproject.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieproject.data.repository.MoviesRepository
import javax.inject.Inject

class DetailViewModelFactory @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(moviesRepository) as T
    }
}