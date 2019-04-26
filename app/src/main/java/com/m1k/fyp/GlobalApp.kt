package com.m1k.fyp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

object GlobalApp : Application() {
    //"cache" variables for DB
    private var loggedIn: String? = null
    const val CAM_REQ = 0
    var draw_vib = false
    var vib = false

    var t2s = false
    var calSw = false

    fun getLogged(): String? {
        return loggedIn
    }

    fun setLogged(s: String) {
        loggedIn = s
    }

    fun isLogged(): Boolean {
        return (loggedIn != null)
    }

    fun logOut() {
        loggedIn = null
        draw_vib = false
        vib = false
        t2s = false
        calSw = false
        calSession = Calender()
        weekSession = Week()
    }

    @SuppressLint("NewApi")
    //vibrate on touch
    fun vibrate(i: Int = 1, context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(
            VibrationEffect.createWaveform(longArrayOf(0, 25), intArrayOf(0, i), -1)
        )
    }

    // "cache" variables for Calendar
    var calSession: Calender? = Calender()
    var weekSession: Week? = Week()
}

