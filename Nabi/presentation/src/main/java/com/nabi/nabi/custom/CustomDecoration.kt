package com.nabi.nabi.custom

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nabi.nabi.utils.DisplayUtils

class CustomDecoration(
    private val dividerHeight: Float,
    private val dividerColor: Int
) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        myDivider(c, parent, color = dividerColor)
    }

    private fun myDivider(c: Canvas, parent: RecyclerView, color: Int) {
        paint.color = color

        val marginPx = DisplayUtils.dpToPx(parent.context, 8f)

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val param = child.layoutParams as RecyclerView.LayoutParams

            // Adjust the position of the divider
            val dividerTop = child.bottom + param.bottomMargin + marginPx
            val dividerBottom = dividerTop + dividerHeight

            c.drawRect(
                child.left.toFloat(),
                dividerTop,
                child.right.toFloat(),
                dividerBottom,
                paint
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val marginPx = DisplayUtils.dpToPx(parent.context, 8f)
        outRect.bottom = (dividerHeight + 2 * marginPx).toInt()
    }
}
