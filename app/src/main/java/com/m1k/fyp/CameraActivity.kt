package com.m1k.fyp

import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_camera.*


@Suppress("DEPRECATION") //camera2 has too much of a learning curve, and is also superfluous for my needs
class CameraActivity : AppCompatActivity() {
    var numOfCams = Camera.getNumberOfCameras()
    var lastCam = 0

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance, front
        } catch (e: Exception) {
            null
        }
    }

    private var noOfCams = Camera.getNumberOfCameras()

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Create an instance of Camera
        mCamera = getCameraInstance()

        mPreview = mCamera?.let {
            // Create our Preview view
            CameraPreview(this, it)
        }

        // Set the Preview view as the content of our activity.
        mPreview?.also {
            val preview: FrameLayout = findViewById(R.id.camera_preview)
            preview.addView(it)
        }


        button_swap.setOnClickListener {
            if (mCamera != null) {
                if (numOfCams > 1) {

                    mCamera?.stopPreview()
                    mCamera?.release()
                    mCamera = null

                    if (lastCam == 1) {
                        mCamera = Camera.open(0)
                        lastCam = 0
                    } else {
                        mCamera = Camera.open(1)
                        lastCam = 1
                    }

                    mPreview = mCamera?.let {
                        CameraPreview(this, it)
                    }

                    mPreview?.also {
                        val preview: FrameLayout = findViewById(R.id.camera_preview)
                        preview.addView(it)
                    }
                    mCamera?.startPreview()

                }
            }

        }

        camera_preview.setOnClickListener {
            mCamera?.autoFocus(null)
        }
    }


}
