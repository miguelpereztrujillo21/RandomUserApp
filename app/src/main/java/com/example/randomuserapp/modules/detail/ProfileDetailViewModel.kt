package com.example.randomuserapp.modules.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.randomuserapp.models.User
import com.google.gson.Gson

class ProfileDetailViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getUsers(userJSON: String?) {
        try {
            _user.postValue(Gson().fromJson(userJSON, User::class.java))
        }catch (e: Exception){
            _error.postValue(e)
        }
    }
}