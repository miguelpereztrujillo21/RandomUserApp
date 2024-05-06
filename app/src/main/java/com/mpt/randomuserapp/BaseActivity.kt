package com.mpt.randomuserapp

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun BaseActivity.setStatusBarMargin(view: View) {
        val rectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top

        when (val params = view.layoutParams) {
            is CoordinatorLayout.LayoutParams -> {
                if (params.topMargin != statusBarHeight) {
                    params.topMargin = statusBarHeight
                    view.layoutParams = params
                }
            }
            is ViewGroup.MarginLayoutParams -> {
                if (params.topMargin != statusBarHeight) {
                    params.topMargin = statusBarHeight
                    view.layoutParams = params
                }
            }
        }
    }
}