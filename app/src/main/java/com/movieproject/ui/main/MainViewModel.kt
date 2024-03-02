package com.movieproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieproject.data.repository.MoviesRepository
import com.movieproject.ui.MovieInfo
import com.movieproject.ui.Movie
import com.movieproject.ui.main.UIState.Loading
import kotlinx.coroutines.launch

class MainViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val movieState = MutableLiveData<UIState<List<Movie>>>()
    fun getMovieData(): LiveData<UIState<List<Movie>>> = movieState


    private val movieInfoState = MutableLiveData<UIState<MovieInfo>>()
    fun getMovieInfoData(): LiveData<UIState<MovieInfo>> = movieInfoState

    init {
        loadData()
    }

    fun loadData() {
        movieState.value = Loading

        viewModelScope.launch {
            movieState.value = moviesRepository.getMoviesData().mapRepositoryToUIState()
        }
    }

    fun loadMovieInfo(movieId: Int) {
        movieInfoState.value = Loading

        viewModelScope.launch {
            movieInfoState.value = moviesRepository.getMovieInfoData(movieId).mapRepositoryToUIState()
        }
    }
}