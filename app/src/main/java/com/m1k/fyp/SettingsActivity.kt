package com.m1k.fyp

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Switch


class Settings(var draw_vibrate : Boolean, var general_vibrate : Boolean, var txt2Speech : Boolean, var calWeekly : Boolean)

class SettingsActivity : AppCompatActivity() {

    private var _draw_vib = false
    private var _vib = false
    private var _t2s = false
    private var _c2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val loggedIn = GlobalApp.getLogged()

        _draw_vib =  GlobalApp.draw_vib
        _vib = GlobalApp.vib
        _t2s = GlobalApp.t2s
        _c2 = GlobalApp.c2


        val dv_s = findViewById<Switch>(R.id.vibDrawSwitch)
        dv_s.isChecked = _draw_vib
        val v_s = findViewById<Switch>(R.id.vibGenSwitch)
        v_s.isChecked = _vib
        val t2s_s = findViewById<Switch>(R.id.enableTxt2SpeechSwitch)
        t2s_s.isChecked = _t2s
        val c2_s = findViewById<Switch>(R.id.calWeeklySwitch)
        c2_s.isChecked = _c2

        dv_s.setOnCheckedChangeListener { _, isChecked ->
            _draw_vib = isChecked

            if (loggedIn != null) {
                WriteDVToDB(loggedIn,_draw_vib).execute()
            }
            GlobalApp.draw_vib = isChecked
        }


        v_s.setOnCheckedChangeListener { _, isChecked ->
            _vib = isChecked
            if (loggedIn != null) {
                WriteGVToDB(loggedIn,_vib).execute()
            }
            GlobalApp.vib = isChecked
        }


        t2s_s.setOnCheckedChangeListener { _, isChecked ->
            _t2s = isChecked
            if (loggedIn != null) {
                WriteT2SToDB(loggedIn,_t2s).execute()
            }
            GlobalApp.t2s = isChecked
        }


        c2_s.setOnCheckedChangeListener { _, isChecked ->
            _c2 = isChecked
            if (loggedIn != null) {
                WriteCalWeeklyToDB(loggedIn,_c2).execute()
            }
            GlobalApp.c2 = isChecked
        }

   }


    inner class WriteDVToDB(val name: String, private val v: Boolean) : AsyncTask<String, Int, Unit>() {
        private val db = UserDataBase.getDatabase(this@SettingsActivity).userDataDao()

        override fun doInBackground(vararg params: String?) {
            return db.updateDrawVibByUser(name, v)
        }
    }

    inner class WriteGVToDB(private val name: String, private val v: Boolean) : AsyncTask<String, Int, Unit>() {
        private val db = UserDataBase.getDatabase(this@SettingsActivity).userDataDao()

        override fun doInBackground(vararg params: String?) {
            return db.updateGenVibByUser(name,v)
        }
    }

    inner class WriteT2SToDB(private val name: String, private val v: Boolean) : AsyncTask<String, Int, Unit>() {
        private val db = UserDataBase.getDatabase(this@SettingsActivity).userDataDao()

        override fun doInBackground(vararg params: String?) {
            return db.updateT2sByUser(name,v)
        }
    }

    inner class WriteCalWeeklyToDB(private val name: String, private val v: Boolean) : AsyncTask<String, Int, Unit>() {
        private val db = UserDataBase.getDatabase(this@SettingsActivity).userDataDao()

        override fun doInBackground(vararg params: String?) {
            return db.updateCalWeeklyByUser(name,v)
        }
    }
}
