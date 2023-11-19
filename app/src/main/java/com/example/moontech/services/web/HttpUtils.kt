package com.example.moontech.services.web

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post

suspend inline fun <reified R> HttpClient.getResult(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> = runCatching { get(urlString, builder).body() }

suspend inline fun <reified R> HttpClient.postResult(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> {
    return try {
        Result.success(post(urlString, builder).body())
    } catch(exception: ResponseException) {
        Result.failure(exception)
    }
}