package com.m1k.fyp

import android.app.Application

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
        return loggedIn == null
    }

    fun logOut() {
        loggedIn = null
        draw_vib = false
        vib = false
        t2s = false
        c2 = false
    }
}

