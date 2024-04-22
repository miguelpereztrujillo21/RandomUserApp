package com.example.randomuserapp.modules.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import com.example.randomuserapp.BaseActivity
import com.example.randomuserapp.R
import com.example.randomuserapp.adapters.UserAdapter
import com.example.randomuserapp.api.ApiRepository
import com.example.randomuserapp.api.ApiRepositoryImpl
import com.example.randomuserapp.databinding.ActivityMainBinding
import com.example.randomuserapp.helpers.Constants
import com.example.randomuserapp.helpers.PopupMenuHelper
import com.example.randomuserapp.helpers.RetrofitHelper
import com.example.randomuserapp.helpers.Utils
import com.example.randomuserapp.modules.detail.ProfileDetailActivity
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: BaseActivity(){
    @Inject
    lateinit var utils: Utils
    @Inject
    lateinit var apiRepository: ApiRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var adapter: UserAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val viewModelFactory = MainViewModelFactory(apiRepository)
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
            adapter?.addLoadStateListener { loaderState ->
                val refreshState = loaderState.refresh
                if (refreshState is LoadState.Error) {
                    val errorMessage = refreshState.error.localizedMessage
                    viewModel.handleException(Throwable(errorMessage))
                }
            }
        }
        viewModel.filterEmail.observe(this) {
            viewModel.getUsersPagerFlow()
        }
        viewModel.filterGender.observe(this) {
            viewModel.getUsersPagerFlow()
        }
        viewModel.error.observe(this) {
            if (it is UnknownHostException) {
                utils.showDialog(this, false, getString(R.string.error_not_internet))
            } else {
                utils.showDialog( this, false, getString(R.string.error_dialog_description))
            }
        }
    }

    private fun initComponents() {
        initToolbar()
        setUpRecycler()
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
            setStatusBarMargin(toolbarContainer)
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
        chip.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onChipCheckedChanged(isChecked,filter)
        }
    }
}