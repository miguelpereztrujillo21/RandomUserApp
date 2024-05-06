package com.mpt.randomuserapp.api

import com.mpt.randomuserapp.models.UserResponse
import com.google.gson.JsonParser
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(private val api: Api) : ApiRepository {
    override suspend fun getUsers(page: Int?, results:Int?, gender: String?): UserResponse {
        return try {
            val response = api.getUsers(page,results,gender)
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
