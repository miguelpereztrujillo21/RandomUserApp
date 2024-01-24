package com.example.randomuserapp.modules.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.randomuserapp.api.ApiRepository

class MainViewModelFactory(private val apiRepository: ApiRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(apiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
