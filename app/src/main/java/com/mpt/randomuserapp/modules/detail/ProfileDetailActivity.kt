package com.mpt.randomuserapp.modules.detail

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mpt.randomuserapp.BaseActivity
import com.mpt.randomuserapp.R
import com.mpt.randomuserapp.databinding.ActivityDetailProfileBinding
import com.mpt.randomuserapp.helpers.Constants
import com.mpt.randomuserapp.helpers.Utils
import com.mpt.randomuserapp.models.User
import javax.inject.Inject

class ProfileDetailActivity : BaseActivity(), OnMapReadyCallback {

    @Inject
    lateinit var utils: Utils
    private lateinit var binding: ActivityDetailProfileBinding
    private var user: User? = null
    private  lateinit var viewModel: ProfileDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_detail_profile)
        setStatusBarMargin(binding.toolbarProfileDetail.toolbarContainer)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[ProfileDetailViewModel::class.java]
        utils = Utils(this)
        getExtras()
        initObservers()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title
        binding.mapDetailProfile.onCreate(savedInstanceState)
        binding.mapDetailProfile.getMapAsync(this)
    }

    private fun initObservers(){
        viewModel.user.observe(this){
            user = it
            initComponents()
        }
    }
    private fun initComponents() {
        initToolbar()
        setUpTextViews()
        Glide.with(this).load(user?.picture?.medium)
            .into(binding.profileImageDetailProfile)
    }

    private fun initToolbar() {
        binding.toolbarProfileDetail.apply {
            title.apply {
                text = utils.getUserFullName(user)
                setTextColor(getColor(R.color.white))
            }
            backButton.apply {
                utils.changueColorDrawable(
                    drawable, ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                setOnClickListener {
                    finish()
                }
            }
            utils.changueColorDrawable(
                optionsButton.drawable, ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )
        }
    }

    private fun setUpTextViews() {
        binding.nameDetailProfile.setUpData(
            R.drawable.ic_user,
            getString(R.string.detail_name),
            utils.getUserFullName( user)
        )
        binding.emailDetailProfile.setUpData(
            R.drawable.ic_mail,
            getString(R.string.detail_email),
            user?.email
        )
        binding.genderDetailProfile.setUpData(
            R.drawable.ic_gender,
            getString(R.string.detail_gender),
            utils.formarGender(user?.gender)
        )
        binding.regDateDetailProfile.setUpData(
            R.drawable.calendar_icon,
            getString(R.string.detail_reg_date),
            utils.formatDateString(user?.registered?.date)
        )
        binding.phoneDetailProfile.setUpData(
            R.drawable.ic_phone,
            getString(R.string.detail_phone),
            user?.cell
        )
        binding.textViewDirection.text = getString(R.string.detail_direccion)
    }

    private fun getExtras() {
        val userJSON = intent.extras?.getString(Constants.BUNDLE_KEY_USER)
        viewModel.getUsers(userJSON)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        user?.location?.coordinates?.let {
            val latLng = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f))
        }
    }

    override fun onDestroy() {
        binding.mapDetailProfile.onDestroy()
        super.onDestroy()
    }


}