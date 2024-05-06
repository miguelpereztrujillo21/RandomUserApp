package com.mpt.randomuserapp

import android.graphics.Rect
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


open class BaseActivity : AppCompatActivity() {

    fun BaseActivity.setStatusBarMargin(view: View) {
        view.post {
            val rectangle = Rect()
            val window = window
            window.decorView.getWindowVisibleDisplayFrame(rectangle)
            val statusBarHeight = rectangle.top

            when (val params = view.layoutParams) {
                is ConstraintLayout.LayoutParams -> {
                    if (params.topMargin != statusBarHeight) {
                        params.topMargin = statusBarHeight
                        view.layoutParams = params
                    }
                }
            }
        }
    }
}