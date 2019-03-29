package com.m1k.fyp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drawing.*
import kotlinx.android.synthetic.main.draw_swipe_menu.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


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

    private var maxY = 9999


    fun setColour(c: Int) {
        colour = c
    }

    init {
        val wr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = DisplayMetrics()

        wr.defaultDisplay.getMetrics(d)

        maxY = d.heightPixels - 196

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
            vibrate(100)
        /*} else if (mCurX - x > 50 || mCurY - y > 50) {
            vibrate(25)
        } else if (mCurX - x > 100 || mCurY - y > 100) {
            vibrate(50)
        } else if (mCurX - x > 200 || mCurY - y > 200) {
            vibrate(75)
        } else if (mCurX - x > 250 || mCurY - y > 250) {
            vibrate(100)
        } else if (mCurX - x > 300 || mCurY - y > 300) {
            vibrate(150)
        } else if (mCurX - x > 400 || mCurY - y > 400) {
            vibrate(250)
        */ }  else vibrate()
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

        //val distance = Math.sqrt((mCurX - mStartX).toDouble().pow(2) + (mCurY - mStartY).toDouble().pow(2))

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (y >= maxY) {
                    invalidate()
                    return false
                }
                mStartX = x
                mStartY = y
                actionDown(x, y)
            }
            MotionEvent.ACTION_MOVE -> actionMove(x, y)
            MotionEvent.ACTION_UP -> {
                if (y >= maxY) {
                    invalidate()
                    return false
                }
                actionUp()
            }
        }
        invalidate()
        return true
    }

    @SuppressLint("NewApi")
    //change intensity based on speed
    fun vibrate(i: Int = 1) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(
            VibrationEffect.createWaveform(longArrayOf(0, 125), intArrayOf(0, i), -1)
        )
    }

    fun saveCanvas() {
        val bitM = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val tempCan = Canvas(bitM)
        tempCan.drawColor(Color.WHITE)

        this.draw(tempCan)
        val s: String = if (GlobalApp.isLogged()) {
            Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).toString() + "/FYP/Public/${GlobalApp.getLogged()}/FYPDrawing_${Calendar.getInstance().time}.png"
        } else {
            Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).toString() + "/FYP/FYPDrawing_${Calendar.getInstance().time}.png"
        }

        val writeTo = File(s)

        try {
            bitM.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(writeTo))
            Toast.makeText(context, "Image Saved to $s", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button2.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.BLUE)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button3.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.GREEN)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button4.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.MAGENTA)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button5.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.RED)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button6.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.LTGRAY)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button7.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.BLACK)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        saveButt.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).saveCanvas()
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

     }

}
