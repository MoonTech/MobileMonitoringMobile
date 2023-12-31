package com.example.moontech.services.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Duration

object HttpClientFactory {
    fun create(
        baseUrl: String,
        tokenManager: TokenManager,
        cookieStorage: CookiesStorage
    ): HttpClient {
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

            install(HttpCookies) {
                storage = cookieStorage
            }

            install(Auth) {
                bearer {
                    sendWithoutRequest {
                        !it.url.pathSegments.contains("watch") &&
                                !it.url.encodedPath.startsWith("/room/refreshToken")
                    }
                    loadTokens {
                        tokenManager.loadTokens()
                    }
                    refreshTokens {
                        tokenManager.refreshTokens(this)
                    }
                }
            }
        }
    }
}