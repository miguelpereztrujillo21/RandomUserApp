package com.example.randomuserapp.modules.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.randomuserapp.R
import com.example.randomuserapp.api.ApiRepositoryImpl
import com.example.randomuserapp.api.RetrofitApiService
import com.example.randomuserapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val apiRepository = ApiRepositoryImpl(RetrofitApiService.getInstance())
    private val viewModelFactory = MainViewModelFactory(apiRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.lifecycleOwner = this
        binding.mainActivity = this
        binding.mainViewModel = viewModel

        viewModel.getUsers()
    }


}