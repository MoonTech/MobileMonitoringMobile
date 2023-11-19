package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.User
import com.example.moontech.data.dataclasses.UserData
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class UserApiServiceImpl(private val httpClient: HttpClient): UserApiService {
    companion object {
        private const val endpoint = "/user"
    }
    override suspend fun register(user: User): Result<UserData> {
        return httpClient.postResult(endpoint) {
            setBody(user)
        }
    }

    override suspend fun logIn(user: User): Result<UserData> {
        return httpClient.postResult("$endpoint/login") {
            setBody(user)
        }
    }

    override suspend fun logOut() {
        TODO("Not yet implemented")
    }
}