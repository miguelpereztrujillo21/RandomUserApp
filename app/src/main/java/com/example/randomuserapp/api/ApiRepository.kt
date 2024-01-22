package com.example.randomuserapp.api

import com.example.randomuserapp.models.UserResponse

interface ApiRepository {
    suspend fun getCharacters(
        page: Int? = null
    ): UserResponse
}