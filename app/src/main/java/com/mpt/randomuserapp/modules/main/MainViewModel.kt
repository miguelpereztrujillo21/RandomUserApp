package com.mpt.randomuserapp.modules.main

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mpt.randomuserapp.api.UserPagingSourceFactory
import com.mpt.randomuserapp.data.UsersPreferences
import com.mpt.randomuserapp.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userPagingSourceFactory: UserPagingSourceFactory,
    private val usersPreferences: UsersPreferences) : ViewModel() {

    private val _filterEmail = MutableLiveData<String>()
    val filterEmail: LiveData<String> get() = _filterEmail
    private val _filterGender = MutableLiveData(usersPreferences.getGenderFilter() ?: "")
    val filterGender: LiveData<String> get() = _filterGender
    private val _users = MutableLiveData<PagingData<User>>()
    val users: LiveData<PagingData<User>> get() = _users
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun setFilterEmail(email: String) {
        _filterEmail.value = email
    }

    fun getUsersPagerFlow() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("RandomUserApp", "Error en la coroutine del MainViewModel", throwable)
            handleException(throwable)
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                userPagingSourceFactory.filterEmail = _filterEmail.value
                userPagingSourceFactory.filterGender = _filterGender.value
                Pager(
                    config = PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = { userPagingSourceFactory() }
                ).flow
                    .cachedIn(this)
                    .catch { e ->
                        Log.e("RandomUserApp", "Error en el Pager", e)
                        handleException(e)
                    }
                    .collectLatest { pagingData ->
                        _users.postValue(pagingData)
                    }
            } catch (e: Throwable) {
                Log.e("RandomUserApp", "Error en el catch del ViewModel", e)
                handleException(e)
            }
        }
    }

    fun handleException(e: Throwable) {
        _error.postValue(e)
    }

    fun onChipCheckedChanged(isChecked: Boolean, filter: String) {
        val newFilterValue = if (isChecked) filter else ""
        if(_filterGender.value != newFilterValue){
            _filterGender.value = newFilterValue
            usersPreferences.setGenderFilter(_filterGender.value ?: "");
        }
    }

}
