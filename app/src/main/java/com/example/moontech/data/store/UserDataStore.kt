package com.example.moontech.data.store

import com.example.moontech.data.dataclasses.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataStore {
    val userData: Flow<UserData?>
    suspend fun save(userData: UserData)
    suspend fun clear()
}