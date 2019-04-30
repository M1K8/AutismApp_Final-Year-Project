package com.m1k.fyp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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

    //initialise variables
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


    //used to change colour
    fun setColour(c: Int) {
        colour = c
    }

    //set the dead zone  at the bottom of the screen based on device resolution
    init {
        val wr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = DisplayMetrics()

        wr.defaultDisplay.getMetrics(d)

        maxY = d.heightPixels - 75

        mPaint.apply {
            color = colour
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 8f
            isAntiAlias = true
        }
    }

    //define behaviour of the "pen"
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //make sure all pre-existing paths are kept when drawing new line
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

        //vibrate if enabled in settings
        if (GlobalApp.draw_vib || GlobalApp.vib)
            GlobalApp.vibrate(50, context)
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


    //save the image in the relevant folder
    fun saveCanvas() {
        val bitM = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val tempCan = Canvas(bitM)
        tempCan.drawColor(Color.WHITE)

        this.draw(tempCan)
        val s = if (GlobalApp.isLogged()) {
            context.getExternalFilesDir("")?.toString() + "/${GlobalApp.getLogged()}"
        } else {
            context.getExternalFilesDir("")?.toString() + "/Public"
        }


        val writeTo = File(s, "FYPDrawing_${Calendar.getInstance().time}.png")


        if (!writeTo.parentFile.exists())
            writeTo.parentFile.mkdirs()

        writeTo.createNewFile()


        try {
            bitM.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(writeTo))
            Toast.makeText(context, "Image Saved to $s", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}

class DrawingActivity : AppCompatActivity() {

    //define our colour menu
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    //add fade animation when colour menu is dragged up
    inner class BCall : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(p0: View, p1: Int) {
            when (p1) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    findViewById<View>(R.id.shadow).visibility = View.VISIBLE
                    findViewById<View>(R.id.shadow).alpha = 1f
                }
                BottomSheetBehavior.STATE_EXPANDED -> findViewById<View>(R.id.shadow).visibility = View.INVISIBLE
                BottomSheetBehavior.STATE_HALF_EXPANDED -> findViewById<View>(R.id.shadow).alpha = 0.4f
            }

        }

        override fun onSlide(p0: View, p1: Float) {
            findViewById<View>(R.id.shadow).alpha = 0.6f
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        //get file permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                GlobalApp.CAM_REQ
            )
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(draw_swipe_menu)

        sheetBehavior.setBottomSheetCallback(BCall())

        //link all the colour buttons
        button1.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.YELLOW)
            if (GlobalApp.vib)
                GlobalApp.vibrate(15, this)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button2.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.BLUE)
            if (GlobalApp.vib)
                GlobalApp.vibrate(15, this)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button3.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.GREEN)
            if (GlobalApp.vib)
                GlobalApp.vibrate(15, this)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button4.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.MAGENTA)
            if (GlobalApp.vib)
                GlobalApp.vibrate(15, this)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button5.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.RED)
            if (GlobalApp.vib)
                GlobalApp.vibrate(15, this)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button6.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.LTGRAY)
            if (GlobalApp.vib)
                GlobalApp.vibrate(15, this)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        button7.setOnClickListener {
            findViewById<DrawView>(R.id.drawing_view).setColour(Color.BLACK)
            if (GlobalApp.vib)
                GlobalApp.vibrate(15, this)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        saveButt.setOnClickListener {
            if (GlobalApp.vib)
                GlobalApp.vibrate(45, this)
            findViewById<DrawView>(R.id.drawing_view).saveCanvas()
        }

     }

}
