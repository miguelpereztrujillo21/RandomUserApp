package com.example.randomuserapp.modules.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.randomuserapp.R
import com.example.randomuserapp.adapters.UserAdapter
import com.example.randomuserapp.api.ApiRepositoryImpl
import com.example.randomuserapp.databinding.ActivityMainBinding
import com.example.randomuserapp.helpers.Constants
import com.example.randomuserapp.helpers.PopupMenuHelper
import com.example.randomuserapp.helpers.RetrofitHelper
import com.example.randomuserapp.helpers.Utils
import com.example.randomuserapp.modules.detail.ProfileDetailActivity
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val apiRepository = ApiRepositoryImpl(RetrofitHelper.getInstance())
    private val viewModelFactory = MainViewModelFactory(apiRepository)
    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.lifecycleOwner = this
        binding.mainActivity = this
        binding.mainViewModel = viewModel

        viewModel.getUsersPagerFlow()
        initObservers()
        initComponents()
    }

    private fun initObservers() {
        viewModel.users.observe(this) {
            adapter?.submitData(lifecycle, it)
        }
        viewModel.filterEmail.observe(this) {
            viewModel.getUsersPagerFlow()
        }
        viewModel.filterGender.observe(this) {
            viewModel.getUsersPagerFlow()
        }
        viewModel.error.observe(this) {
            if (it is UnknownHostException) {
                Utils.showDialog(this, this, false, getString(R.string.error_not_internet))
            } else {
                Utils.showDialog(this, this, false, getString(R.string.error_dialog_description))
            }
        }
    }

    private fun initComponents() {
        setUpRecycler()
        initToolbar()
        initChips()
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            title.text = getString(R.string.simple_contacts)
            optionsButton.setOnClickListener {
                PopupMenuHelper.showMenu(applicationContext, it, R.menu.users_menu)
                PopupMenuHelper.onEmailClickListener = {
                    binding.searchContainerMain.visibility = View.VISIBLE
                }
                PopupMenuHelper.onGenderClickListener = {
                    binding.layoutFilters.chipGroupGenderMain.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpRecycler() {
        adapter = UserAdapter(this, object : UserAdapter.ClickListener {
            override fun onClick(position: Int) {
                val intent = Intent(this@MainActivity, ProfileDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putString(
                    Constants.BUNDLE_KEY_USER,
                    Gson().toJson(adapter?.getUserPosition(position))
                )
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        binding.recyclerMain.adapter = adapter
    }

    private fun initChips() {
        initChip(binding.layoutFilters.chipGenderMaleMain, Constants.MALE_KEY)
        initChip(binding.layoutFilters.chipGenderFemaleMain, Constants.FEMALE_KEY)
    }

    private fun initChip(chip: Chip, filter: String) {
        chip.setOnCheckedChangeListener { _, _ ->
            viewModel.filterGender.value = filter
        }
    }
}