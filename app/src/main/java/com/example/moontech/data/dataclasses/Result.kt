package com.example.moontech.data.dataclasses

sealed class Result<T> {
    data class Loading<T>(val isLoading: Boolean = true): Result<T>()
    data class Success<T>(val data: T): Result<T>()
    data class Error<T>(val throwable: Throwable): Result<T>()
    class Empty<T>(): Result<T>()
}