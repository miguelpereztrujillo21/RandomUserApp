package com.mpt.randomuserapp.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mpt.randomuserapp.databinding.ProfileTextViewBinding

class ProfileTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ProfileTextViewBinding =
        ProfileTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setUpData(iconResId: Int, title: String, value: String?) {
        binding.textViewIcon.setImageResource(iconResId)
        binding.textViewTitle.text = title
        binding.textViewValue.text = value ?: ""
    }
}