package com.example.proiectpiu_managementfinanciar.reports

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomPieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private var data = listOf<Triple<Float, Int, String>>()
    private var selectedIndex: Int? = null
    private var onSliceSelected: ((Int, Triple<Float, Int, String>) -> Unit)? = null

    fun setOnSliceSelectedListener(listener: (Int, Triple<Float, Int, String>) -> Unit) {
        onSliceSelected = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val total = data.sumOf { it.first.toDouble() }.toFloat()
        var startAngle = 0f

        rectF.set(
            width * 0.2f,
            height * 0.2f,
            width * 0.8f,
            height * 0.8f
        )

        data.forEachIndexed { index, (value, color, _) ->
            val sweepAngle = (value / total) * 360f
            paint.color = color

            if (selectedIndex == index) {
                paint.setShadowLayer(15f, 0f, 0f, Color.BLACK)
                canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
                paint.clearShadowLayer()
            } else {
                canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
            }

            startAngle += sweepAngle
        }
    }

    fun setData(newData: List<Triple<Float, Int, String>>) {
        data = newData
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x - width / 2
            val y = event.y - height / 2

            var angle = Math.toDegrees(Math.atan2(y.toDouble(), x.toDouble()))
            if (angle < 0) {
                angle += 360
            }

            val total = data.sumOf { it.first.toDouble() }.toFloat()
            var startAngle = 0f

            data.forEachIndexed { index, (value, _, _) ->
                val sweepAngle = (value / total) * 360f

                if (angle >= startAngle && angle < startAngle + sweepAngle) {
                    selectedIndex = index
                    invalidate()
                    onSliceSelected?.invoke(index, data[index])
                    return true
                }

                startAngle += sweepAngle
            }
        }
        return super.onTouchEvent(event)
    }

}
