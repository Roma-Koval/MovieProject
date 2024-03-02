package com.movieproject.ui.main

sealed class UIState<out T : Any>{
    class Success<T : Any>(val data: T): UIState<T>()

    data object Loading : UIState<Nothing>()

    class Error(val error: Throwable) : UIState<Nothing>()
}
