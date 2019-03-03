package com.m1k.fyp

import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class GlobalApp : Application() {


    companion object {
        private var loggedIn : String? = null
        fun getLogged(): String? {
            return loggedIn
        }

        fun setLogged(s: String) {
            loggedIn = s
        }

        var draw_vib  = false
        var vib = false

        var t2s = false
        var c2 = false
    }
}

class Home : AppCompatActivity() {

    val db = UserDataBase.getDatabase(this, null).userDataDao()

    inner class GetSettingsFromDB(s : String) : AsyncTask<String, Int, Settings?>() {
        val name = s

        override fun doInBackground(vararg params: String?): Settings? {
            return db.getSettingsByName(name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val y = GlobalScope.async {
            val c = Calender("w","m","l","a","e","d","b")
            val w = Week("w","m","l","a","e","d","b")
            var e = User(0,"Yeetus",true,true,true,false, null,c,w)
            db.insert(e)
            GlobalApp.setLogged("Yeetus")
        }
        runBlocking {
            y.await()
        }


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
            startActivity(intent)
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
}