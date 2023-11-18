package com.example.moontech.data.repository

import com.example.moontech.data.dataclasses.User

class InMemoryUserRepository: UserRepository {
    companion object {
        private val users: MutableSet<User> = mutableSetOf()
    }

    override suspend fun register(user: User): Boolean {
        if (users.contains(user)) {
            return false
        }
        users.add(user)
        return true
    }

    override suspend fun login(user: User): Boolean {
        if (users.contains(user)) {
            return true
        }
        return false
    }

    override suspend fun logout() {
    }
}