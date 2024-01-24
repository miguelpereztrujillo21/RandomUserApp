package com.example.randomuserapp.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.randomuserapp.models.User
import com.example.randomuserapp.models.UserResponse
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.Flow

class ApiRepositoryImpl(private val api: Api) : ApiRepository {
    override suspend fun getUsers(page: Int?): UserResponse {
        return try {
            val response = api.getUsers(page)
            if (response.isSuccessful) {
                response.body() ?: UserResponse()
            } else {
                val jsonObject = JsonParser().parse(response.errorBody()?.string()).asJsonObject
                val errorValue = jsonObject.get("error").asString
                throw Exception("Error en la solicitud: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
