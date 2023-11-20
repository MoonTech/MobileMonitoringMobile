package com.example.moontech.data.dataclasses

sealed class AppError() {
    class Error(val errorMessage: String): AppError()
    class Empty : AppError()

    fun isError(): Boolean {
        return this is AppError.Error
    }

    suspend fun ifError(block: suspend (error: Error) -> Unit) {
        if (isError()) {
            block(this as Error)
        }
    }
}