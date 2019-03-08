package com.m1k.fyp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.draw_swipe_menu.*


//from https://android.jlelse.eu/a-guide-to-drawing-in-android-631237ab6e28
class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mPaint = Paint()
    private var mPath = Path()
    private var paths: MutableList<Path> = mutableListOf()
    private var colour = Color.BLACK
    private var colorsMap: MutableMap<Path, Int> = mutableMapOf()

    private var mCurX = 0f
    private var mCurY = 0f
    private var mStartX = 0f
    private var mStartY = 0f

    fun setColour(c: Int) {
        colour = c
    }

    init {
        mPaint.apply {
            color = colour
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 8f
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (p in paths) {
            mPaint.color = colorsMap[p]!!
            canvas.drawPath(p, mPaint)
        }
        mPaint.color = colour
        canvas.drawPath(mPath, mPaint)
    }

    private fun actionDown(x: Float, y: Float) {
        mPath.moveTo(x, y)
        mCurX = x
        mCurY = y
    }

    private fun actionMove(x: Float, y: Float) {
        mPath.quadTo(mCurX, mCurY, (x + mCurX) / 2, (y + mCurY) / 2)

        if (mCurX - x > 20 || mCurY - y > 20) {
            vibrate(2)
        } else if (mCurX - x > 50 || mCurY - y > 50) {
            vibrate(5)
        } else if (mCurX - x > 100 || mCurY - y > 100) {
            vibrate(10)
        } else vibrate()
        mCurX = x
        mCurY = y

    }

    private fun actionUp() {
        mPath.lineTo(mCurX, mCurY)

        // draw a dot on click
        if (mStartX == mCurX && mStartY == mCurY) {
            mPath.lineTo(mCurX, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY)
        }

        paths.add(mPath)
        colorsMap[mPath] = colour

        mPath = Path()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = x
                mStartY = y
                actionDown(x, y)
            }
            MotionEvent.ACTION_MOVE -> actionMove(x, y)
            MotionEvent.ACTION_UP -> actionUp()
        }
        //vibrate(0)
        invalidate()
        return true
    }

    @SuppressLint("NewApi")
    //change intensity based on speed
    fun vibrate(i: Int = 1) {
        var v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(
            VibrationEffect.createWaveform(longArrayOf(0, 175), intArrayOf(0, i), -1)
        )
    }
}

class DrawingActivity : AppCompatActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(draw_swipe_menu)

        button1.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.YELLOW)
        }

        button2.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.BLUE)
        }

        button3.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.GREEN)
        }

        button4.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.MAGENTA)
        }

        button5.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.RED)
        }

        button6.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.LTGRAY)
        }

        button7.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.BLACK)
        }

     }

}
