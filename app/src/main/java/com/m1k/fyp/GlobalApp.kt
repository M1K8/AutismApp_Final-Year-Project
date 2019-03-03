package com.m1k.fyp

import android.app.Application

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