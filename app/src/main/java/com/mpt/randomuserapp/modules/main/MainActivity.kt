package com.mpt.randomuserapp.modules.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import com.mpt.randomuserapp.BaseActivity
import com.mpt.randomuserapp.R
import com.mpt.randomuserapp.adapters.UserAdapter
import com.mpt.randomuserapp.databinding.ActivityMainBinding
import com.mpt.randomuserapp.helpers.Constants
import com.mpt.randomuserapp.helpers.PopupMenuHelper
import com.mpt.randomuserapp.helpers.Utils
import com.mpt.randomuserapp.modules.detail.ProfileDetailActivity
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.mpt.randomuserapp.data.UsersPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: BaseActivity(){
    @Inject
    lateinit var utils: Utils
    @Inject
    lateinit var userPreferences: UsersPreferences
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.mainActivity = this
        binding.mainViewModel = viewModel

        viewModel.getUsersPagerFlow()
        initObservers()
        initComponents()
    }

    override fun onStart(){
        super.onStart()
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
            binding.layoutFilters.chipGroupGenderMain.visibility = if (it != "") View.VISIBLE else View.GONE;
            binding.layoutFilters.chipGenderMaleMain.isChecked = it == Constants.MALE_KEY
            binding.layoutFilters.chipGenderFemaleMain.isChecked = it == Constants.FEMALE_KEY
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
        onBackPress()
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
        }
        setBackButtonFunctionality(binding.toolbar.backButton)
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
        adapter?.addLoadStateListener { loadState ->
            val isLoading = loadState.refresh is LoadState.NotLoading
            if(!isLoading ){
                adapter?.isLoading = true
            }
        }
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

     private fun onBackPress() {
         onBackPressedDispatcher.addCallback(this) {
             if (binding.searchContainerMain.visibility == View.VISIBLE) {
                 binding.searchContainerMain.visibility = View.GONE
             } else if (binding.layoutFilters.chipGroupGenderMain.visibility == View.VISIBLE) {
                 binding.layoutFilters.chipGroupGenderMain.visibility = View.GONE
             } else {
                 finish()
             }
         }
    }
}