package com.example.randomuserapp.api

import com.example.randomuserapp.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET
    suspend fun getUsers(@Query("page") page: Int? = null): Response<UserResponse>

}