package com.m1k.fyp

import android.content.res.Resources
import android.os.AsyncTask
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import java.util.*

class CalenderActivity : AppCompatActivity(), TextToSpeech.OnInitListener  {
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            tts.language = Locale.UK
        }
    }

    var calSession : Calender? = null
    var weekSession : Week? = null
    var tts = TextToSpeech(this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        val loggedIn = GlobalApp.getLogged()

        if (GlobalApp.c2) {
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
        } else {
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
        if (loggedIn != null) {
            val calProm = GetCalenderFromDB(loggedIn).execute()
            val c = calProm.get()

            val weekProm = GetWeekFromDB(loggedIn).execute()
            val w = weekProm.get()

            fun t2s(s : String) {
                tts.speak(s,TextToSpeech.QUEUE_FLUSH, null, "")
            }

            if (GlobalApp.c2) {
                var txt = findViewById<EditText>(R.id.edit1)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(w?.monday)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }
                txt = findViewById(R.id.edit2)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(w?.tuesday)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit3)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(w?.wednesday)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit4)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(w?.thursday)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit5)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(w?.friday)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit6)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(w?.saturday)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit7)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(w?.sunday)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }
            } else {
                var txt = findViewById<EditText>(R.id.edit1)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(c?.wakeUp)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit2)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(c?.morning)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit3)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(c?.lunchTime)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit4)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(c?.afternoon)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit5)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(c?.dinnerTime)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }

                txt = findViewById(R.id.edit6)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(c?.evening)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }
                txt = findViewById(R.id.edit7)
                txt.apply {
                    text = Editable.Factory.getInstance().newEditable(c?.bedTime)
                    maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                    setOnLongClickListener {
                        t2s(txt.text.toString())
                        true
                    }
                }
            }

        }


    }

    inner class GetCalenderFromDB(val name: String) : AsyncTask<String, Int, Calender?>() {
        private val db = UserDataBase.getDatabase(this@CalenderActivity).userDataDao()
        override fun doInBackground(vararg params: String?): Calender? {
            calSession = db.getCalenderByName(name)

            return calSession
        }

    }

    inner class GetWeekFromDB(val name: String) : AsyncTask<String, Int, Week?>() {
        private val db = UserDataBase.getDatabase(this@CalenderActivity).userDataDao()
        override fun doInBackground(vararg params: String?): Week? {
            weekSession = db.getWeekByName(name)

            return weekSession
        }

    }

}
