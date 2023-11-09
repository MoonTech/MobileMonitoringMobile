package com.example.moontech.data.repository

import com.example.moontech.data.dataclasses.User

interface UserRepository {
    suspend fun register(user: User): Boolean
    suspend fun login(user: User): Boolean
    suspend fun logout()
}