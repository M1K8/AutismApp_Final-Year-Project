package com.m1k.fyp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.content_home.*
import java.util.*

class Home : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var loggedIn = GlobalApp.getLogged()

    //initialise text to speech engine
    private var tts: TextToSpeech? = null
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set UK English as language for tts
            tts?.language = Locale.UK
        }
    }

    //helper method for t2s
    fun t2s(s: String) {
        tts?.speak(s, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun initVals() {
        //redraw based on login condition - show logout button only when a user is logged in
        loggedIn = GlobalApp.getLogged()
        if (loggedIn != null) {
            val settProm = GetSettingsFromDB(loggedIn!!).execute()
            val s = settProm.get()

            if (s != null) {
                GlobalApp.draw_vib = s.draw_vibrate
                GlobalApp.vib = s.general_vibrate
                GlobalApp.t2s = s.txt2Speech
                GlobalApp.c2 = s.calWeekly
                GlobalApp.t2s = s.txt2Speech
            }

            findViewById<TextView>(R.id.welcomeText).text = "Welcome, $loggedIn !"
            findViewById<TextView>(R.id.welcomeText).invalidate()


        } else {
            findViewById<Button>(R.id.logOutButt).visibility = GONE
            findViewById<TextView>(R.id.welcomeText).text = ""
            findViewById<TextView>(R.id.welcomeText).invalidate()
            this.findViewById<Button>(R.id.logOutButt).invalidate()
        }
    }

    //handle the logout press to redraw activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                this.findViewById<Button>(R.id.logOutButt).visibility = VISIBLE
                this.findViewById<Button>(R.id.logOutButt).invalidate()

                initVals()
            }
        }
    }

    //vibrate on every tap if enabled
    override fun onResume() {
        if (GlobalApp.vib) {
            findViewById<View>(R.id.homePg).setOnTouchListener { v, event ->

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        GlobalApp.vibrate(20, v.context)
                    }
                }

                super.onTouchEvent(event)
            }
        }

        //enable t2s on hold if enabled
        if (GlobalApp.t2s) {
            if (tts == null)
                tts = TextToSpeech(this, this)

            loginButton.setOnLongClickListener {
                t2s("Login")
                true
            }

            settingsButton.setOnLongClickListener {
                t2s("Settings")
                true
            }

            drawButton.setOnLongClickListener {
                t2s("Draw")
                true
            }

            camButton.setOnLongClickListener {
                t2s("Camera")
                true
            }

            pecsButton.setOnLongClickListener {
                t2s("Pecs")
                true
            }

            calButton.setOnLongClickListener {
                t2s("Calender")
                true
            }

            strgButton.setOnLongClickListener {
                t2s("Saved Pictures")
                true
            }

            if (GlobalApp.isLogged()) {
                welcomeText.setOnLongClickListener {
                    t2s(welcomeText.text.toString())
                    true
                }
            }
        } else {
            //disable t2s is disabled
            loginButton.setOnLongClickListener { false }
            settingsButton.setOnLongClickListener { false }
            drawButton.setOnLongClickListener { false }
            camButton.setOnLongClickListener { false }
            pecsButton.setOnLongClickListener { false }
            calButton.setOnLongClickListener { false }
            strgButton.setOnLongClickListener { false }
            welcomeText.setOnLongClickListener { false }

            if (tts != null) {
                tts?.stop()
                tts?.shutdown()
            }
        }


        super.onResume()
    }


    //clean up t2s engine to prevent leak
    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }
   override fun onCreate(savedInstanceState: Bundle?) {

       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_home)


       initVals()

       //link the applets

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

       calButton.setOnClickListener {
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

           initVals()
       }

       strgButton.setOnClickListener {
           val intent = Intent(this, FileExplorer::class.java)
           startActivity(intent)
       }

   }


    //helper class to access db
    inner class GetSettingsFromDB(val name: String) : AsyncTask<String, Int, Settings?>() {
        private val db = UserDataBase.getDatabase(this@Home).userDataDao()
        override fun doInBackground(vararg params: String?): Settings? {
            return db.getSettingsByName(name)
        }
    }
}