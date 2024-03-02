package com.movieproject.data.repository

import com.movieproject.ui.main.UIState

sealed class RepositoryResult<out T : Any>{

    data class Success<T : Any>(val result: T): RepositoryResult<T>()

    data class Error(val error: Throwable): RepositoryResult<Nothing>()

    fun mapRepositoryToUIState(): UIState<T> {
        return when (this) {
            is Success -> UIState.Success(result)
            is Error -> UIState.Error(error)
        }
    }
}
