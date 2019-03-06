package com.m1k.fyp

import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.content_home.*

class Home : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                this.recreate()
            }
        }
    }
   override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val loggedIn = GlobalApp.getLogged()

        if (loggedIn != null) {
            val settProm = GetSettingsFromDB(loggedIn).execute()
            val s = settProm.get()

            if (s != null) {
                GlobalApp.draw_vib = s.draw_vibrate
                GlobalApp.vib = s.general_vibrate
                GlobalApp.t2s = s.txt2Speech
                GlobalApp.c2 = s.calWeekly
            }

        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            var req = 0
            startActivityForResult(intent, req)

        }

        camButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        drawButton.setOnClickListener {
            val intent = Intent(this, DrawingActivity::class.java)
            startActivity(intent)
        }


        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        calButton.setOnClickListener{
            val intent = Intent(this, CalenderActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }

    }

    inner class GetSettingsFromDB(s : String) : AsyncTask<String, Int, Settings?>() {
        val name = s
        val db = UserDataBase.getDatabase(this@Home, null).userDataDao()
        override fun doInBackground(vararg params: String?): Settings? {
            return db.getSettingsByName(name)
        }
    }
}