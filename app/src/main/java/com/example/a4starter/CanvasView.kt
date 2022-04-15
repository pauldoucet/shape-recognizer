package com.example.a4starter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    val path = Path()
    var pointMode = false
    var pointArray = ArrayList<Point>()
    
    init {
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        setBackgroundColor(Color.CYAN)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event!!.y

        if(event.action == MotionEvent.ACTION_DOWN) {
            path.moveTo(x, y)
        } else if(event.action == MotionEvent.ACTION_MOVE) {
            path.lineTo(x, y)
        }

        postInvalidate()
        return true
    }

    fun sample() : ArrayList<Point> {
        return PathSampler.samplePath(path)
    }

    fun drawArrayPoints(canvas: Canvas?, points: ArrayList<Point>) {
        if(points.size > 0) {
            val point = points[0]
            canvas!!.drawCircle(point.x, point.y, 10f, paint)
        }

        for(i in 1 until points.size) {
            val point = points[i]
            canvas!!.drawCircle(point.x, point.y,5f, paint)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if(pointMode) {
            drawArrayPoints(canvas, pointArray)
        } else {
            canvas!!.drawPath(path, paint)
        }
    }

    fun clear() {
        path.reset()
        postInvalidate()
    }
}