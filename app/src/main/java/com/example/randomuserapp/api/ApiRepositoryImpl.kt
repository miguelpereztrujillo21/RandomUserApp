package com.example.randomuserapp.api

import com.example.randomuserapp.models.UserResponse
import com.google.gson.JsonParser

class ApiRepositoryImpl(private val api: Api) : ApiRepository {
    override suspend fun getCharacters(page: Int?): UserResponse {


        return try {
            val response = api.getUsers(page)
            if (response.isSuccessful) {
                response.body() ?: UserResponse()
            } else {
                val jsonObject = JsonParser().parse(response.errorBody()?.string()).asJsonObject
                val errorValue = jsonObject.get("error").asString
              /*  if (errorValue?.equals(Constants.NOT_RESULTS) == true) {
                    throw Exception(Constants.NOT_RESULTS)
                } else {
                    throw Exception("Error en la solicitud: ${response.code()}")
                }*/
                throw Exception("Error en la solicitud: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}