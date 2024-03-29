package com.example.randomuserapp.api

import com.example.randomuserapp.models.UserResponse

interface ApiRepository {
    suspend fun getUsers(
        page: Int? = null,
        results: Int? = null,
        gender:String? = null
    ): UserResponse
}