package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.User
import com.example.moontech.data.dataclasses.UserData

interface UserApiService {
    suspend fun register(user: User): Result<UserData>

    suspend fun logIn(user: User): Result<UserData>

    suspend fun logOut()
}