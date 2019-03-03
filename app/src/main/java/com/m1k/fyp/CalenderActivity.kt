package com.m1k.fyp

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class CalenderActivity : AppCompatActivity() {

    var calSession : Calender? = null
    val db = UserDataBase.getDatabase(this, null).userDataDao()

    inner class GetCalenderFromDB(s : String) : AsyncTask<String, Int, Calender?>() {
        val name = s

        override fun doInBackground(vararg params: String?): Calender? {
            calSession = db.getCalenderByName(name)

            return calSession
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        var loggedIn = GlobalApp.getLogged()

        if (loggedIn != null) {
            val calProm = GetCalenderFromDB(loggedIn).execute()
            val c = calProm.get()
            Toast.makeText(this, c?.bedTime, Toast.LENGTH_LONG).show()
        }
        // read from db, if !null then populate

    }
}
