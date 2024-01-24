package com.example.randomuserapp.modules.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp.api.ApiRepository
import com.example.randomuserapp.models.User
import kotlinx.coroutines.launch

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    var users = MutableLiveData<ArrayList<User>>()
    var error = MutableLiveData<String>()
    private var currentPage = 1
    private var isLoading = false

    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getUsers(page = currentPage)
                isLoading = true
            } catch (e: Exception) {
                handleException(e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun handleException(e: Exception) {
        error.postValue("Error: ${e.message}")
        users.postValue(ArrayList())

    }
}