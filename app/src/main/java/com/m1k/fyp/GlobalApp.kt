package com.m1k.fyp

import android.app.Application

object GlobalApp : Application() {
        private var loggedIn : String? = null
        val CAM_REQ = 0
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