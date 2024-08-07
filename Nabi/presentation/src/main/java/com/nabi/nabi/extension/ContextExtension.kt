package com.nabi.nabi.extension

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import kotlin.time.times

fun Context.dialogResize(dialog: Dialog, width: Float, height: Float? = null){
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT < 30){
        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val window = dialog.window

        val x = (size.x * width).toInt()
        val y = if(height == null) WRAP_CONTENT else (size.y * height).toInt()

        window?.setLayout(x, y)

    } else{
        val rect = windowManager.currentWindowMetrics.bounds

        val window = dialog.window
        val x = (rect.width() * width).toInt()
        val y = if(height == null) WRAP_CONTENT else (rect.height() * height).toInt()

        window?.setLayout(x, y)
    }
}