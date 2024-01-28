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
       // initToolbar(fullName)
        setUpTextViews(user)
        Glide.with(this).load(user?.picture?.medium)
            .into(binding.profileImageDetailProfile)
    }

/*    private fun initToolbar(fullname: String) {
        binding.toolbar.apply {
            title.text = fullname.uppercase()
            backButton.setOnClickListener {
                finish()
            }
        }
    }*/

    private fun setUpTextViews(user: User?) {
        binding.profileNameDetailProfile.setUpData(
            R.drawable.user_icon,
            getString(R.string.detail_name),
            Utils.getUserFullName(this,user))
        binding.profileEmailDetailProfile.setUpData(
            R.drawable.mail_icon, getString(R.string.detail_email), user?.email)
        binding.profileGenderDetailProfile.setUpData(
            R.drawable.gender_icon,
            getString(R.string.detail_gender),
            Utils.formarGender(this,user?.gender))
        binding.profileRegDateDetailProfile.setUpData(
            R.drawable.calendar_icon,
            getString(R.string.detail_reg_date),
            Utils.formatDateString(user?.registered?.date))
        binding.profilePhoneDetailProfile.setUpData(
            R.drawable.phone_icon,
            getString(R.string.detail_phone),
            user?.cell)

        binding.textViewDirection.text = getString(R.string.detail_direccion)
    }


    private fun getExtras() {
        val userJSON = intent.extras?.getString(Constants.BUNDLE_KEY_USER)
        initComponents(Gson().fromJson(userJSON, User::class.java))
    }

}