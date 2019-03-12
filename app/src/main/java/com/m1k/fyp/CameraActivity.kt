package com.m1k.fyp

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.FileOutputStream
import java.util.*


@Suppress("DEPRECATION") //camera2 has too much of a learning curve, and is also superfluous for my needs
class CameraActivity : AppCompatActivity() {
    private var numOfCams = Camera.getNumberOfCameras()
    private var lastCam = 0

    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance, front
        } catch (e: Exception) {
            null
        }
    }
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            GlobalApp.CAM_REQ -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    finish()
                } else init()
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), GlobalApp.CAM_REQ)
        }
        setContentView(R.layout.activity_camera)
        init()

    }

    override fun onRestart() {
        super.onRestart()
        init()
    }

    inner class PictureDoneCallback : Camera.PictureCallback {

        inner class SaveAsync : AsyncTask<ByteArray, Int, Unit>() {
            override fun doInBackground(vararg params: ByteArray) {
                val picFile: String = if (GlobalApp.isLogged()) {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/${GlobalApp.getLogged()}/FYPDrawing_${Calendar.getInstance().time}.png"
                } else {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/FYPDrawing_${Calendar.getInstance().time}.png"
                }

                try {
                    val fos = FileOutputStream(picFile)
                    fos.write(params[0])
                    fos.close()

                } catch (e: Exception) {
                    //Looper.prepare()
                    Toast.makeText(this@CameraActivity, e.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        override fun onPictureTaken(data: ByteArray, camera: Camera) {
            try {
                SaveAsync().execute(data)
                Toast.makeText(this@CameraActivity, "Picture Taken", Toast.LENGTH_LONG).show()

                mCamera?.startPreview()

            } catch (e: Exception) {
                //Looper.prepare()
                Toast.makeText(this@CameraActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun init() {
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


        button_capture.setOnClickListener {
            if (mCamera != null) {
                mCamera!!.takePicture(null, null, PictureDoneCallback())

            }
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
