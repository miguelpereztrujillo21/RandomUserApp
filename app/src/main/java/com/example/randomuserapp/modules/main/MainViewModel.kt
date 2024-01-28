package com.example.randomuserapp.modules.main

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.randomuserapp.api.ApiRepository
import com.example.randomuserapp.api.UserPagingSource
import com.example.randomuserapp.models.User

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {


    private var error = MutableLiveData<String>()
    val users: LiveData<PagingData<User>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UserPagingSource(apiRepository) }
    ).flow
        .cachedIn(viewModelScope)
        .asLiveData()

    private fun handleException(e: Exception) {
        error.postValue("Error: ${e.message}")
       // users.postValue(ArrayList())

    }
}