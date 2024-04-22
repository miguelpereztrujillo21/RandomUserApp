package com.example.randomuserapp.api

import javax.inject.Inject

class UserPagingSourceFactory @Inject constructor(
    private val apiRepository: ApiRepository
) {
    fun create(filterEmail: String?, filterGender: String?): UserPagingSource {
        return UserPagingSource(apiRepository, filterEmail, filterGender)
    }
}