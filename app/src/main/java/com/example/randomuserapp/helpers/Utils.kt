package com.example.randomuserapp.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.randomuserapp.R
import com.example.randomuserapp.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Utils @Inject constructor(@ApplicationContext private val context: Context) {

    fun getUserFullName( user: User?): String {
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

    fun formarGender(gender: String?): String? {
        return when (gender) {
            Constants.MALE_KEY -> context.getString(R.string.simple_male)
            Constants.FEMALE_KEY -> context.getString(R.string.simple_female)
            else -> gender
        }
    }

    fun changueColorDrawableWhite(drawable: Drawable) {
        drawable.let {
            DrawableCompat.setTint(
                it, ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }
    }

    fun showDialog(
        activity: Activity,
        finishBtn: Boolean,
        description: String,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.error_dialog_title))
        builder.setMessage(description)
        if (finishBtn) {
            builder.setPositiveButton(context.getString(R.string.error_dialog_close_btn)) { _, _ ->
                activity.finishAffinity()
            }
        } else {
            builder.setPositiveButton(context.getString(R.string.error_dialog_acept_btn_not_results)) { dialog, _ ->
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}