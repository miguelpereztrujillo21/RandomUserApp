package com.example.randomuserapp.helpers

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu

import com.example.randomuserapp.R

object PopupMenuHelper : PopupMenu.OnMenuItemClickListener {

     var onEmailClickListener: (() -> Unit)? = null
     var onNameClickListener: (() -> Unit)? = null

    fun showMenu(context: Context, v: View, menuResId: Int) {
        PopupMenu(context, v).apply {
            setOnMenuItemClickListener(this@PopupMenuHelper)
            inflate(menuResId)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_email -> {
                onEmailClickListener?.invoke()
                true
            }

            R.id.menu_name -> {
                onNameClickListener?.invoke()
                true
            }

            else -> false
        }
    }
}