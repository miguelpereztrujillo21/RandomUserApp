package com.example.randomuserapp.modules.main

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.randomuserapp.api.ApiRepository
import com.example.randomuserapp.api.UserPagingSource
import com.example.randomuserapp.models.User
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {


    val filterEmail = MutableLiveData<String>()
    val filterGender = MutableLiveData<String>()
    private val _users = MutableLiveData<PagingData<User>>()
    val users: LiveData<PagingData<User>> get() = _users
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun createPagerFlow() {
        val exceptionHandler = CoroutineExceptionHandler{_ , throwable->
            throwable.printStackTrace()
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val userPagingSource =
                    UserPagingSource(apiRepository, filterEmail.value, filterGender.value)
                Pager(
                    config = PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = { userPagingSource }
                ).flow
                    .cachedIn(this)
                    .catch { e ->
                        Log.e("RandomUserApp", "Error en el Pager", e)
                    }
                    .collectLatest { pagingData ->
                        _users.postValue(pagingData)
                    }
            } catch (e: Throwable) {
                Log.e("MyApp", "Error en el catch del ViewModel", e)
            }
        }
    }

    private fun handleException(exception: Throwable) {
        _error.postValue(exception.message)
    }

    /*    fun update() {
            _users.value = createPagerFlow().value
        }*/
}