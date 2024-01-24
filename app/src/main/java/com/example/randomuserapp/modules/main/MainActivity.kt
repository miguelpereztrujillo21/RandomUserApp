package com.example.randomuserapp.modules.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuserapp.R
import com.example.randomuserapp.adapters.UserAdapter
import com.example.randomuserapp.api.ApiRepositoryImpl
import com.example.randomuserapp.api.RetrofitApiService
import com.example.randomuserapp.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val apiRepository = ApiRepositoryImpl(RetrofitApiService.getInstance())
    private val viewModelFactory = MainViewModelFactory(apiRepository)

    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.lifecycleOwner = this
        binding.mainActivity = this
        binding.mainViewModel = viewModel

        initObservers()
        setUpRecycler()
        viewModel.getUsers()
    }

    private fun initObservers(){
        viewModel.users.observe(this){
            adapter?.submitList(it)
        }
    }
    private fun setUpRecycler() {
        adapter = UserAdapter(this, object : UserAdapter.ClickListener {
            override fun onClick(position: Int) {

            }
        })
        binding.recyclerMain.adapter = adapter
    }


}