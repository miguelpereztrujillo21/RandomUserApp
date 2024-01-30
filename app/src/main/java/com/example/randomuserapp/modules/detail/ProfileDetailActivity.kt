package com.example.randomuserapp.modules.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.bumptech.glide.Glide
import com.example.randomuserapp.R
import com.example.randomuserapp.databinding.ActivityDetailProfileBinding
import com.example.randomuserapp.helpers.Constants
import com.example.randomuserapp.helpers.Utils
import com.example.randomuserapp.models.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson


class ProfileDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailProfileBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_detail_profile)
        binding.lifecycleOwner = this

        getExtras()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title
        binding.mapDetailProfile.onCreate(savedInstanceState)
        binding.mapDetailProfile.getMapAsync(this)

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
                text = Utils.getUserFullName(applicationContext, user).uppercase()
                setTextColor(getColor(R.color.white))
            }
            backButton.apply {
                Utils.changueColorDrawableWhite(applicationContext, drawable)
                setOnClickListener {
                    finish()
                }
            }
            Utils.changueColorDrawableWhite(applicationContext, optionsButton.drawable)
        }
    }

    private fun setUpTextViews() {
        binding.nameDetailProfile.setUpData(
            R.drawable.ic_user,
            getString(R.string.detail_name),
            Utils.getUserFullName(this, user)
        )
        binding.emailDetailProfile.setUpData(
            R.drawable.ic_mail,
            getString(R.string.detail_email),
            user?.email
        )
        binding.genderDetailProfile.setUpData(
            R.drawable.ic_gender,
            getString(R.string.detail_gender),
            Utils.formarGender(this, user?.gender)
        )
        binding.regDateDetailProfile.setUpData(
            R.drawable.calendar_icon,
            getString(R.string.detail_reg_date),
            Utils.formatDateString(user?.registered?.date)
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
        user = Gson().fromJson(userJSON, User::class.java)
        initComponents()
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