package com.movieproject.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieproject.data.repository.MoviesRepository
import com.movieproject.ui.MovieInfo
import com.movieproject.ui.main.UIState
import com.movieproject.ui.main.UIState.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val movieInfoState = MutableStateFlow<UIState<MovieInfo>>(Loading)
    fun getState(): StateFlow<UIState<MovieInfo>> = movieInfoState.asStateFlow()

    fun loadMovieInfo(movieId: Int) {
        movieInfoState.value = Loading

        viewModelScope.launch {
            movieInfoState.value = moviesRepository.getMovieInfoData(movieId).mapRepositoryToUIState()
        }
    }
}