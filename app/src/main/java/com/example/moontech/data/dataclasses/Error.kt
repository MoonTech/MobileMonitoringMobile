package com.example.moontech.data.dataclasses

sealed class AppState() {
    class Error(val errorMessage: String): AppState()
    class Empty : AppState()

    class Loading: AppState()

    fun isError(): Boolean {
        return this is AppState.Error
    }

    fun isLoading(): Boolean {
        return this is AppState.Loading
    }

    suspend fun ifError(block: suspend (error: Error) -> Unit) {
        if (isError()) {
            block(this as Error)
        }
    }

    suspend fun ifLoading(block: suspend (state: Loading) -> Unit) {
        if (isLoading()) {
            block(this as Loading)
        }
    }
}