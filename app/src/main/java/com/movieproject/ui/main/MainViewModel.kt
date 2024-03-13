package com.movieproject.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movieproject.data.repository.MoviesRepository
import com.movieproject.ui.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val movieState = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    fun getPagingDataState(): StateFlow<PagingData<Movie>> = movieState.asStateFlow()

    init {
        loadMovieData()
    }

    fun loadMovieData() {
        viewModelScope.launch {
            moviesRepository.getPagedMovies()
                .cachedIn(viewModelScope)
                .collect {
                    movieState.value = it
                }
        }
    }
}