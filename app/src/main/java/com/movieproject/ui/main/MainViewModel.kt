package com.movieproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movieproject.data.repository.MoviesRepository
import com.movieproject.ui.Movie
import kotlinx.coroutines.launch

class MainViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val movieState = MutableLiveData<PagingData<Movie>>()
    fun getMoviePagingDataState(): LiveData<PagingData<Movie>> = movieState

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