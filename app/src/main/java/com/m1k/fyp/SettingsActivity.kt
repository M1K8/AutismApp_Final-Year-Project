package com.m1k.fyp

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Switch


class Settings(var draw_vibrate : Boolean, var general_vibrate : Boolean, var txt2Speech : Boolean, var calWeekly : Boolean)

class SettingsActivity : AppCompatActivity() {

    private var _draw_vib = false
    private var _vib = false
    private var _t2sSw = false
    private var _c2 = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<View>(R.id.settings).setOnClickListener {
            if (GlobalApp.vib)
                GlobalApp.vibrate(25, this)
        }

        if (GlobalApp.vib) {
            findViewById<View>(R.id.settTab).setOnTouchListener { v, event ->

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        GlobalApp.vibrate(20, v.context)
                    }
                }

                super.onTouchEvent(event)
            }
        }


        //set switches to reflect the current values
        val loggedIn = GlobalApp.getLogged()

        _draw_vib =  GlobalApp.draw_vib
        _vib = GlobalApp.vib
        _t2sSw = GlobalApp.t2sSw
        _c2 = GlobalApp.calSw


        val dv_s = findViewById<Switch>(R.id.vibDrawSwitch)
        dv_s.isChecked = _draw_vib
        val v_s = findViewById<Switch>(R.id.vibGenSwitch)
        v_s.isChecked = _vib
        val t2s_s = findViewById<Switch>(R.id.enableTxt2SpeechSwitch)
        t2s_s.isChecked = _t2sSw
        val c2_s = findViewById<Switch>(R.id.calWeeklySwitch)
        c2_s.isChecked = _c2

        //when each switch is changed, first update the "cached" settings, then write to db
        dv_s.setOnCheckedChangeListener { _, isChecked ->
            _draw_vib = isChecked
            if (GlobalApp.vib)
                GlobalApp.vibrate(30, this)

            if (loggedIn != null) {
                WriteDVToDB(loggedIn,_draw_vib).execute()
            }
            GlobalApp.draw_vib = isChecked
        }


        v_s.setOnCheckedChangeListener { _, isChecked ->
            _vib = isChecked
            if (GlobalApp.vib)
                GlobalApp.vibrate(30, this)
            if (loggedIn != null) {
                WriteGVToDB(loggedIn,_vib).execute()
            }
            GlobalApp.vib = isChecked

            if (GlobalApp.vib) {
                findViewById<View>(R.id.settTab).setOnTouchListener { v, event ->

                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            GlobalApp.vibrate(20, v.context)
                        }
                    }

                    super.onTouchEvent(event)
                }
            }

        }


        t2s_s.setOnCheckedChangeListener { _, isChecked ->
            _t2sSw = isChecked
            if (GlobalApp.vib)
                GlobalApp.vibrate(30, this)
            if (loggedIn != null) {
                WriteT2SToDB(loggedIn, _t2sSw).execute()
            }
            GlobalApp.t2sSw = isChecked
        }


        c2_s.setOnCheckedChangeListener { _, isChecked ->
            _c2 = isChecked
            if (GlobalApp.vib)
                GlobalApp.vibrate(30, this)
            if (loggedIn != null) {
                WriteCalWeeklyToDB(loggedIn,_c2).execute()
            }
            GlobalApp.calSw = isChecked
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
