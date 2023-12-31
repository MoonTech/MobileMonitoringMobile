package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.UserData
import com.example.moontech.data.store.UserDataStore
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.header
import io.ktor.client.statement.request
import kotlinx.coroutines.flow.first

class TokenManager(private val userDataStore: UserDataStore) {
    suspend fun loadTokens(): BearerTokens? {
        val userData = userDataStore.userData.first()
        return userData?.accessToken?.let { BearerTokens(it, it) }
    }

    suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens? {
        val userData = userDataStore.userData.first() ?: return null
        val response = params.client.postResult<UserData>("/user/refreshToken") {
            header("Authorization", "Bearer ${params.oldTokens?.accessToken ?: userData.accessToken}")
        }
        response.fold(
            onSuccess = {
                userDataStore.save(it)
                return BearerTokens(it.accessToken, it.accessToken)
            },
            onFailure = {
                if (!params.response.request.url.encodedPath.startsWith("/room/watch")) {
                    userDataStore.clear()
                }
                return null
            }
        )
    }
}