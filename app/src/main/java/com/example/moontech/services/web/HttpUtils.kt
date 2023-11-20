package com.example.moontech.services.web

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post

suspend inline fun <reified R> HttpClient.getResult(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> {
    return withErrorHandling {
        get(urlString, builder).body()
    }
}

suspend inline fun <reified R> HttpClient.postResult(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> {
    return withErrorHandling {
        post(urlString, builder).body()
    }
}

suspend inline fun HttpClient.deleteWithStatus(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<Boolean> {
    return withErrorHandling {
        delete(urlString, builder).status.value in 200..299
    }
}

suspend inline fun <reified R> HttpClient.withErrorHandling(block: HttpClient.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch(exception: ResponseException) {
        Result.failure(exception)
    } catch (t: Throwable) {
        Log.e("http", "postResult: ", t)
        Result.failure(t)
    }
}