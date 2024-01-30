package com.example.randomuserapp.api

import com.example.randomuserapp.models.UserResponse
import com.google.gson.JsonParser

class ApiRepositoryImpl(private val api: Api) : ApiRepository {
    override suspend fun getUsers(page: Int?, results:Int?): UserResponse {
        return try {
            val response = api.getUsers(page,results)
            if (response.isSuccessful) {
                response.body() ?: UserResponse()
            } else {
                val jsonObject = JsonParser().parse(response.errorBody()?.string()).asJsonObject
                val errorValue = jsonObject.get("error").asString
                throw Exception("Error en la solicitud: ${response.code()} + $errorValue")
            }
        } catch (e: Exception) {
            throw e
        }
    }

}
