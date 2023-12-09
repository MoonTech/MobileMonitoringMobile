package com.example.moontech.services.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Duration

object HttpClientFactory {
    fun create(baseUrl: String, tokenManager: TokenManager): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true
            engine {
                config {
                    connectTimeout(Duration.ofSeconds(5))
                }
                addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                url(baseUrl)
            }

            install(ContentNegotiation) {
                json()
            }

            install(Auth) {
                bearer {
                    sendWithoutRequest { !it.url.pathSegments.contains("watch") }
                    loadTokens {
                        tokenManager.loadTokens()
                    }
                    refreshTokens {
                        tokenManager.refreshTokens(oldTokens)
                    }
                }
            }
        }
    }
}