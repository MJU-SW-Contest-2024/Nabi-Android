package com.nabi.nabi.utils

import android.content.Context
import android.util.DisplayMetrics

object DisplayUtils {

    /**
     * Convert dp to pixels.
     * @param context The context to access resources and display metrics.
     * @param dp The value in dp (density-independent pixels) to convert.
     * @return The equivalent value in pixels.
     */
    fun dpToPx(context: Context, dp: Float): Float {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        return dp * displayMetrics.density
    }

    /**
     * Convert pixels to dp.
     * @param context The context to access resources and display metrics.
     * @param px The value in pixels to convert.
     * @return The equivalent value in dp (density-independent pixels).
     */
    fun pxToDp(context: Context, px: Float): Float {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        return px / displayMetrics.density
    }
}