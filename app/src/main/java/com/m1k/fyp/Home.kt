package com.m1k.fyp

import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class GlobalApp : Application() {
    private var loggedIn : String? = null

    fun getLogged() : String? {
        return loggedIn
    }

    fun setLogged(s : String) {
        loggedIn = s
    }
}

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        try {
            val db = UserDataBase.getDatabase(this, null).userDataDao()
            val y = GlobalScope.async {
                val c = Calender("w","m","l","a","e","d","b")
                var e = User(0,"Yeetus",true,true,true,null, c)
                db.insert(e)
                GlobalApp().setLogged("Yeetus")
            }
            runBlocking {
                y.await()
            }
        } catch (e: Exception) {
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
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