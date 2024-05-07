package com.mpt.randomuserapp.data

import com.mpt.randomuserapp.models.User

interface UsersPreferences {
   /* fun getUserList(): List<User>
    fun setUserList(list: List<User>)*/

    fun setGenderFilter(gender: String)
    fun getGenderFilter(): String?
}