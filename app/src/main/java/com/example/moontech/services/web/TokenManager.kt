package com.example.moontech.services.web

import com.example.moontech.data.store.UserDataStore
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.first

class TokenManager(private val userDataStore: UserDataStore) {
    suspend fun loadTokens(): BearerTokens? {
        val userData = userDataStore.userData.first()
        return userData?.accessToken?.let { BearerTokens(it, it) }
    }

    suspend fun refreshTokens(oldTokens: BearerTokens?): BearerTokens? {
        val userData = userDataStore.userData.first()
        // here we treat access token as refresh token
        if (userData == null || oldTokens?.refreshToken == userData.accessToken) {
            userDataStore.clearUserData()
            return null
        }
        return BearerTokens(userData.accessToken, userData.accessToken)
    }
}