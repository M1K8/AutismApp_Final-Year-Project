package com.m1k.fyp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

object GlobalApp : Application() {
    private var loggedIn: String? = null
    const val CAM_REQ = 0
    var draw_vib = false
    var vib = false

    var t2s = false
    var c2 = false

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
        c2 = false
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


    var calSession: Calender? = Calender()
    var weekSession: Week? = Week()
}

