package com.example.moontech.services.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import okhttp3.logging.HttpLoggingInterceptor

object HttpClientFactory {

    fun create(baseUrl: String): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true
            engine {
                addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
            }

            defaultRequest {
                url(baseUrl)
            }

            install(ContentNegotiation) {
                json()
            }
        }
    }
}