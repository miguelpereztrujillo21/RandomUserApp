package com.example.randomuserapp.helpers

import android.content.Context
import com.example.randomuserapp.R
import com.example.randomuserapp.models.User
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun getUserFullName(context: Context,user: User?): String {
        return context.getString(R.string.user_full_name, user?.name?.first, user?.name?.last)
    }

    fun formatDateString(inputDate: String?): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date: Date = inputDate?.let { inputFormat.parse(it) } ?: return ""
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun formarGender(context: Context,gender:String?): String? {
        return when (gender) {
            Constants.MALE_KEY -> context.getString(R.string.simple_male)
            Constants.FEMALE_KEY -> context.getString(R.string.simple_female)
            else -> gender
        }
    }
}