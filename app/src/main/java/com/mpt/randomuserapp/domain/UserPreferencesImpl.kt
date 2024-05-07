package com.mpt.randomuserapp.domain

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mpt.randomuserapp.data.UsersPreferences
import com.mpt.randomuserapp.models.User
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(context: Context): UsersPreferences {
    companion object {
        private const val PREFS_NAME = "com.mpt.randomuserapp.userprefs"
/*        private const val USER_LIST = "UserList"*/
        private const val GENDER_FILTER = "gender_filter"
    }
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
   /* private val gson = Gson()*/
/*
    override fun getUserList(): List<User> {
        val json = prefs.getString(USER_LIST, null)
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type) ?: listOf()
    }

    override fun setUserList(list: List<User>) {
        val json = gson.toJson(list)
        prefs.edit().putString(USER_LIST, json).apply()
    }
*/

    override fun setGenderFilter(gender: String) {
        prefs.edit().putString(GENDER_FILTER, gender).apply()
    }

    override fun  getGenderFilter(): String? {
        return prefs.getString(GENDER_FILTER, null)
    }
}