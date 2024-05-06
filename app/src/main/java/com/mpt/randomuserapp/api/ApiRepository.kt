package com.mpt.randomuserapp.api

import com.mpt.randomuserapp.models.UserResponse

interface ApiRepository {
    suspend fun getUsers(
        page: Int? = null,
        results: Int? = null,
        gender:String? = null
    ): UserResponse
}