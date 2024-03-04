package com.movieproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movieproject.data.repository.MoviesRepository
import com.movieproject.ui.MovieInfo
import com.movieproject.ui.Movie
import com.movieproject.ui.main.UIState.Loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val movieState = MutableLiveData<UIState<PagingData<Movie>>>()
    fun getMovieData(): LiveData<UIState<PagingData<Movie>>> = movieState


    private val movieInfoState = MutableLiveData<UIState<MovieInfo>>()
    fun getMovieInfoData(): LiveData<UIState<MovieInfo>> = movieInfoState

    init {
        loadData()
    }

    fun loadData() {
//        movieState.value = Loading
        viewModelScope.launch {
            moviesRepository.getPagedMovies().cachedIn(viewModelScope)
                .collect {
                    movieState.value = UIState.Success(it)
                }
        }
    }

    fun loadMovieInfo(movieId: Int) {
        movieInfoState.value = Loading

        viewModelScope.launch {
            movieInfoState.value = moviesRepository.getMovieInfoData(movieId).mapRepositoryToUIState()
        }
    }

    suspend fun getMovies(): Flow<PagingData<Movie>> {
        return moviesRepository.getPagedMovies()
            .cachedIn(viewModelScope)
    }
}