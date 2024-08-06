package com.nabi.nabi.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.nabi.nabi.R

class CustomChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        style = Paint.Style.FILL
    }
    private var values = intArrayOf(0, 0, 0, 0, 0)
    private val colors = intArrayOf(
        getColorInt(R.color.red),
        getColorInt(R.color.primary),
        getColorInt(R.color.green),
        getColorInt(R.color.skyblue),
        getColorInt(R.color.purple)
    )

    private fun getColorInt(colorId: Int): Int = ContextCompat.getColor(context, colorId)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val totalWidth = width.toFloat()
        val barCount = values.size
        val sectionWidth = totalWidth / barCount
        val barWidth = sectionWidth * 0.24f

        val cornerRadius = 30f
        val dummyHeight = 20f

        val maxValue = values.maxOrNull() ?: 1
        val scaleFactor = height / maxValue.toFloat()

        for (i in values.indices) {
            val value = values[i]
            val barHeight = (value * scaleFactor).toInt()
            val barColor = colors[i]
            paint.color = barColor

            val sectionCenterX = i * sectionWidth + sectionWidth / 2
            val left = sectionCenterX - barWidth / 2
            val top = height - barHeight.toFloat()
            val right = sectionCenterX + barWidth / 2
            val bottom = height.toFloat()

            canvas.drawRoundRect(
                RectF(left, top, right, bottom),
                cornerRadius, cornerRadius, paint
            )

            if (value != 0) {
                val additionalTop = bottom - dummyHeight

                paint.color = barColor

                canvas.drawRect(
                    RectF(left, additionalTop, right, bottom),
                    paint
                )
            }
        }
    }


    fun setValues(newValues: IntArray) {
        values = newValues
        invalidate()
    }
}
