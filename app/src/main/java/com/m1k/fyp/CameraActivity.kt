package com.m1k.fyp

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


@Suppress("DEPRECATION") //camera2 has too much of a learning curve, and is also superfluous for my needs
class CameraActivity : AppCompatActivity() {

    //initialise values for camera swapping
    private var numOfCams = Camera.getNumberOfCameras()
    private var lastCam = 0


    //make sure cameras are accessible
    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance, front
        } catch (e: Exception) {
            null
        }
    }
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null


    //process request to permissions to access the camera
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

            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get permissions to access the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), GlobalApp.CAM_REQ)
        }
        setContentView(R.layout.activity_camera)
        init()

    }

    //make sure app restart is handled in regard to cameras
    override fun onRestart() {
        super.onRestart()
        init()
    }


    //create callback class to allow image saving
    inner class PictureDoneCallback : Camera.PictureCallback {
        //asynchronously save images from camera to app storage
        inner class SaveAsync : AsyncTask<ByteArray, Int, String>() {
            override fun doInBackground(vararg params: ByteArray): String {
                val picFile = this@CameraActivity.getExternalFilesDir("")?.toString()
                Looper.prepare()

                //save image either under the user specific storage, or the "public" app directory
                val fileStr = if (GlobalApp.isLogged()) {
                    picFile + "/${GlobalApp.getLogged()}"
                } else {
                    "$picFile/Public/"
                }
                Toast.makeText(this@CameraActivity, fileStr, Toast.LENGTH_LONG).show()


                return try {
                    val writeTo = File(fileStr, "Picture_${Calendar.getInstance().time}.png")

                    if (!writeTo.parentFile.exists())
                        writeTo.parentFile.mkdirs()

                    writeTo.createNewFile()

                    val fos = FileOutputStream(writeTo)
                    fos.write(params[0])
                    fos.close()

                    fileStr

                } catch (e: Exception) {
                    Looper.prepare()
                    Toast.makeText(this@CameraActivity, e.message, Toast.LENGTH_LONG).show()
                    ""
                }
            }
        }

        // save the image once the picture has been taken
        override fun onPictureTaken(data: ByteArray, camera: Camera) {
            try {
                val e = SaveAsync().execute(data)

                val str = e.get()
                Toast.makeText(this@CameraActivity, "Picture Taken, saved at $str", Toast.LENGTH_LONG).show()

                mCamera?.startPreview()

            } catch (e: Exception) {
                //Looper.prepare()
                Toast.makeText(this@CameraActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    //make sure camera(s) are initialised properly when the activity is (re)started
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

        //capture the image by providing a callback object
        button_capture.setOnClickListener {
            if (mCamera != null) {
                mCamera!!.takePicture(null, null, PictureDoneCallback())

            }
        }


        //swap cameras, if possible
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


        // autofocus the camera when the screen is tapped
        camera_preview.setOnClickListener {
            mCamera?.autoFocus(null)
        }
    }


}
