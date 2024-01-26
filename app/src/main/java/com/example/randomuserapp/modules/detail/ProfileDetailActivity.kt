package com.example.randomuserapp.modules.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.bumptech.glide.Glide
import com.example.randomuserapp.R
import com.example.randomuserapp.databinding.ActivityDetailProfileBinding
import com.example.randomuserapp.databinding.ProfileTextViewBinding
import com.example.randomuserapp.helpers.Constants
import com.example.randomuserapp.helpers.Utils
import com.example.randomuserapp.models.User
import com.google.gson.Gson


class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private var positionLayout: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_detail_profile)
        getExtras()
    }

    private fun initComponents(user: User?) {
        val fullName = Utils.getUserFullName(this, user)
        initToolbar(fullName)
        setUpTextViews(user)
        Glide.with(this).load(user?.picture?.medium).into(binding.profileImageDetailProfile)
    }

    private fun newTextViewDetail(iconResId: Int, title: String, value: String?) {
        val profileTextViewBinding = ProfileTextViewBinding.inflate(LayoutInflater.from(this))
        profileTextViewBinding.apply {
            textViewIcon.setImageResource(iconResId)
            textViewTitle.text = title
            textViewValue.text = value
        }
        binding.containerDetailProfile.addView(profileTextViewBinding.root, positionLayout)
        positionLayout += 1
    }

    private fun initToolbar(fullname: String) {
        binding.toolbar.title.text = fullname.uppercase()
        binding.toolbar.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setUpTextViews(user: User?) {
        newTextViewDetail(
            R.drawable.user_icon,
            getString(R.string.detail_name),
            Utils.getUserFullName(this, user)
        )
        newTextViewDetail(R.drawable.mail_icon, getString(R.string.detail_email), user?.email)
        newTextViewDetail(
            R.drawable.gender_icon,
            getString(R.string.detail_gender),
            Utils.formarGender(this, user?.gender)
        )
        newTextViewDetail(
            R.drawable.calendar_icon,
            getString(R.string.detail_reg_date),
            Utils.formatDateString(user?.registered?.date)
        )
        newTextViewDetail(R.drawable.phone_icon, getString(R.string.detail_phone), user?.phone)
        binding.textViewDirection.text = getString(R.string.detail_direccion)
    }

    private fun getExtras() {
        val userJSON = intent.extras?.getString(Constants.BUNDLE_KEY_USER)
        initComponents(Gson().fromJson(userJSON, User::class.java))
    }

}