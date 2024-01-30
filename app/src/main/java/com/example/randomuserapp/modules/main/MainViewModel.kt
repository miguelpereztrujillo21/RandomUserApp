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

    val filterEmail = MutableLiveData<String>()
    val filterGender = MutableLiveData<String>()
    private val _users = MutableLiveData<PagingData<User>>()
    val users: LiveData<PagingData<User>> get() = _users

    init {
        _users.value = createPagerFlow().value
    }

    private fun createPagerFlow(): MutableLiveData<PagingData<User>> {
        val liveData = MutableLiveData<PagingData<User>>()
        try {
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { UserPagingSource(apiRepository, filterEmail.value, filterGender.value)}
            ).flow
                .cachedIn(viewModelScope)
                .asLiveData()
                .observeForever {
                    liveData.value = it
                }
        } catch (e: Exception) {

            liveData.value = PagingData.empty()
        }
        return liveData
    }
    fun update() {
        _users.value = createPagerFlow().value
    }
}