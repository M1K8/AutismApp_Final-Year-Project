package com.m1k.fyp

import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.content_home.*

class Home : AppCompatActivity() {
    private var loggedIn = GlobalApp.getLogged()


    private fun init() {
        loggedIn = GlobalApp.getLogged()
        if (loggedIn != null) {
            val settProm = GetSettingsFromDB(loggedIn!!).execute()
            val s = settProm.get()

            if (s != null) {
                GlobalApp.draw_vib = s.draw_vibrate
                GlobalApp.vib = s.general_vibrate
                GlobalApp.t2s = s.txt2Speech
                GlobalApp.c2 = s.calWeekly
            }
        } else {
            findViewById<Button>(R.id.logOutButt).visibility = GONE
            this.findViewById<Button>(R.id.logOutButt).invalidate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                this.findViewById<Button>(R.id.logOutButt).visibility = VISIBLE
                this.findViewById<Button>(R.id.logOutButt).invalidate()
                //this.recreate()
                init()
            }
        }
    }
   override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

       init()

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val req = 0
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

       pecsButton.setOnClickListener {
           val intent = Intent(this, PECSActivity::class.java)
           startActivity(intent)
       }

       logOutButt.setOnClickListener {
           this.findViewById<Button>(R.id.logOutButt).visibility = GONE
           Toast.makeText(this, "User ${GlobalApp.getLogged()} logged out", Toast.LENGTH_SHORT).show()
           GlobalApp.logOut()
           init()
           //this.recreate()
       }
    }



    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.content_home)
        } else if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }

    }

    inner class GetSettingsFromDB(val name: String) : AsyncTask<String, Int, Settings?>() {
        private val db = UserDataBase.getDatabase(this@Home, null).userDataDao()
        override fun doInBackground(vararg params: String?): Settings? {
            return db.getSettingsByName(name)
        }
    }
}