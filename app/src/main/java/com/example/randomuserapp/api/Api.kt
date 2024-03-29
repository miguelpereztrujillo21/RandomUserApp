package com.example.randomuserapp.api

import com.example.randomuserapp.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("api/")
    suspend fun getUsers(
        @Query("page") page: Int? = null,
        @Query("results") results: Int? = null,
        @Query("gender") gender: String? = null
    ): Response<UserResponse>

}