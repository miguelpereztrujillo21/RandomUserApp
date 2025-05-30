package com.mpt.randomuserapp.api

import androidx.paging.PagingSource
import com.mpt.randomuserapp.models.User
import javax.inject.Inject

class UserPagingSourceFactory @Inject constructor(
    private val apiRepository: ApiRepository
) : () -> PagingSource<Int, User> {
    var filterEmail: String? = null
    var filterGender: String? = null

    override fun invoke(): PagingSource<Int, User> {
        return UserPagingSource(apiRepository, filterEmail, filterGender)
    }
}