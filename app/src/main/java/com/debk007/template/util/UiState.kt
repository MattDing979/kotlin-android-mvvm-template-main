package com.debk007.template.util

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()

    data class Error(val errorMsg: String) : UiState<Nothing>()

    data class Loading(val isLoading: Boolean) : UiState<Nothing>()
}