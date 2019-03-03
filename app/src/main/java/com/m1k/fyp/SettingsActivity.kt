package com.m1k.fyp

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Switch
import android.widget.TextView


class Settings(var draw_vibrate : Boolean, var general_vibrate : Boolean, var txt2Speech : Boolean, var calWeekly : Boolean)

class SettingsActivity : AppCompatActivity() {

    val db = UserDataBase.getDatabase(this, null).userDataDao()

    inner class WriteDVToDB(s : String, b : Boolean) : AsyncTask<String, Int, Unit>() {
        val name = s
        val v = b

        override fun doInBackground(vararg params: String?) {
            return db.updateDrawVibByUser(name, v)
        }
    }

    inner class WriteGVToDB(s : String, b : Boolean) : AsyncTask<String, Int, Unit>() {
        val name = s
        val v = b

        override fun doInBackground(vararg params: String?) {
            return db.updateGenVibByUser(name,v)
        }
    }

    inner class WriteT2SToDB(s : String, b : Boolean) : AsyncTask<String, Int, Unit>() {
        val name = s
        val v = b

        override fun doInBackground(vararg params: String?) {
            return db.updateT2sByUser(name,v)
        }
    }

    inner class WriteCalWeeklyToDB(s : String, b : Boolean) : AsyncTask<String, Int, Unit>() {
        val name = s
        val v = b

        override fun doInBackground(vararg params: String?) {
            return db.updateCalWeeklyByUser(name,v)
        }
    }

    var _draw_vib = false
    var _vib = false
    var _t2s = false
    var _c2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var loggedIn = GlobalApp.getLogged()

        _draw_vib =  GlobalApp.draw_vib
        _vib = GlobalApp.vib
        _t2s = GlobalApp.t2s
        _c2 = GlobalApp.c2

        val dv_s = findViewById<Switch>(R.id.vibDrawSwitch)
        val v_s = findViewById<Switch>(R.id.vibGenSwitch)
        val t2s_s = findViewById<Switch>(R.id.enableTxt2SpeechSwitch)
        val c2_s = findViewById<Switch>(R.id.calWeeklySwitch)

        dv_s.setOnCheckedChangeListener{ buttonView, isChecked ->
            _draw_vib = isChecked

            if (loggedIn != null) {
                WriteDVToDB(loggedIn,_draw_vib).execute()
            }
        }


        v_s.setOnCheckedChangeListener{ buttonView, isChecked ->
            _vib = isChecked
            if (loggedIn != null) {
                WriteGVToDB(loggedIn,_vib).execute()
            }
        }


        t2s_s.setOnCheckedChangeListener{ buttonView, isChecked ->
            _t2s = isChecked
            if (loggedIn != null) {
                WriteT2SToDB(loggedIn,_t2s).execute()
            }
        }


        c2_s.setOnCheckedChangeListener{ buttonView, isChecked ->
            _c2 = isChecked
            if (loggedIn != null) {
                WriteCalWeeklyToDB(loggedIn,_c2).execute()
            }
        }


        if (_c2) {
            var switch = findViewById<TextView>(R.id.title1)
            switch.text = "Monday"

            switch = findViewById(R.id.title2)
            switch.text = "Tuesday"

            switch = findViewById(R.id.title3)
            switch.text = "Wednesday"

            switch = findViewById(R.id.title4)
            switch.text = "Thursday"

            switch = findViewById(R.id.title5)
            switch.text = "Friday"

            switch = findViewById(R.id.title6)
            switch.text = "Saturday"

            switch = findViewById(R.id.title7)
            switch.text = "Sunday"
        }
        else {
            var switch = findViewById<TextView>(R.id.title1)
            switch.text = "Wake Up"

            switch = findViewById(R.id.title2)
            switch.text = "Morning"

            switch = findViewById(R.id.title3)
            switch.text = "Lunch"

            switch = findViewById(R.id.title4)
            switch.text = "Afternoon"

            switch = findViewById(R.id.title5)
            switch.text = "Dinner"

            switch = findViewById(R.id.title6)
            switch.text = "Evening"

            switch = findViewById(R.id.title7)
            switch.text = "Bedtime"
        }
    }
}
