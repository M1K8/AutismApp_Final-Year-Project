package com.m1k.fyp

import android.content.res.Resources
import android.os.AsyncTask
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.util.SparseArray
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import com.m1k.fyp.GlobalApp.t2s
import java.util.*

class CalenderActivity : AppCompatActivity(), TextToSpeech.OnInitListener  {

    //define text to speech engine
    private var tts: TextToSpeech? = null

    override fun onResume() {
        if (GlobalApp.vib) {
            findViewById<View>(R.id.calTable).setOnTouchListener { v, event ->
                GlobalApp.vibrate(20, v.context)
                super.onTouchEvent(event)
            }
        }
        super.onResume()
    }

    //set the locale to EN_GB when the textToSpeech engine is initialised
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set UK English as language for tts
            tts?.language = Locale.UK
        }
    }

    //helper method to "speak" a given string


    //create a data structure the allows for efficient storage of pairs of function ptrs, used in saving text box contents
    //Int = R.id, f1() == weekUpdater, f2() == calender updater
    var weekCalPair : SparseArray<Pair<  (s : String) -> Unit  , (s : String) -> Unit   >> = SparseArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        //get the current user
        val loggedIn = GlobalApp.getLogged()

        //set all text text change listeners based on settings
        initBoxChange()

        //initialise textToSpeech engine
        tts = TextToSpeech(this, this)

        //set column headers based on calendar settings
        if (GlobalApp.calSw) {
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

        //if a user is logged in, popluate the text boxes with their saved strings
        if (loggedIn != null) {
            val cProm = GetCalenderFromDB(loggedIn).execute()


            val wProm = GetWeekFromDB(loggedIn).execute()

            cProm.get()

            wProm.get()
        }

        reInit()

    }

    //redraw text boxes based, as boxes need to resixe based on text
    private fun reInit() {
        if (GlobalApp.calSw) {
            var txt = findViewById<EditText>(R.id.edit1)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.weekSession?.monday)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }
            txt = findViewById(R.id.edit2)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.weekSession?.tuesday)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit3)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.weekSession?.wednesday)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit4)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.weekSession?.thursday)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit5)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.weekSession?.friday)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit6)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.weekSession?.saturday)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit7)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.weekSession?.sunday)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }
        } else {
            var txt = findViewById<EditText>(R.id.edit1)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.calSession?.wakeUp)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit2)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.calSession?.morning)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit3)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.calSession?.lunchTime)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit4)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.calSession?.afternoon)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit5)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.calSession?.dinnerTime)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }

            txt = findViewById(R.id.edit6)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.calSession?.evening)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }
            txt = findViewById(R.id.edit7)
            txt.apply {
                text = Editable.Factory.getInstance().newEditable(GlobalApp.calSession?.bedTime)
                maxWidth = Resources.getSystem().displayMetrics.widthPixels / 7
                setOnLongClickListener {
                    t2s(txt.text.toString(), tts)
                    true
                }
            }
        }
    }

    //when the Acitivity is stopped, make sure all text fields are written to the database if the user is logged in
    override fun onStop() {
        super.onStop()
        val loggedIn = GlobalApp.getLogged()

        if (loggedIn != null) {

            if (GlobalApp.calSw) {
                val e = WriteWeekToDB(loggedIn, GlobalApp.weekSession!!).execute()
                e.get()
            } else {
                val e = WriteCalToDB(loggedIn, GlobalApp.calSession!!).execute()
                e.get()
            }
        }
    }

    //when the Activity is destoryed, make sure we dont leak the textToSpeech engine
    override fun onDestroy() {
        if (tts != null) {

            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }

    //helper method to initialise various text changes listener for all text boxes. Handles both calendar settings.
    private fun initBoxChange() {

        findViewById<EditText>(R.id.edit1).addTextChangedListener(Watch(R.id.edit1, R.id.edit1C))
        weekCalPair.append(R.id.edit1,Pair(::updateCal1, ::updateWeek1))

        findViewById<EditText>(R.id.edit2).addTextChangedListener(Watch(R.id.edit2, R.id.edit2C))
        weekCalPair.append(R.id.edit2,Pair(::updateCal2, ::updateWeek2))

        findViewById<EditText>(R.id.edit3).addTextChangedListener(Watch(R.id.edit3, R.id.edit3C))
        weekCalPair.append(R.id.edit3,Pair(::updateCal3, ::updateWeek3))

        findViewById<EditText>(R.id.edit4).addTextChangedListener(Watch(R.id.edit4, R.id.edit4C))
        weekCalPair.append(R.id.edit4,Pair(::updateCal4, ::updateWeek4))

        findViewById<EditText>(R.id.edit5).addTextChangedListener(Watch(R.id.edit5, R.id.edit5C))
        weekCalPair.append(R.id.edit5,Pair(::updateCal5, ::updateWeek5))

        findViewById<EditText>(R.id.edit6).addTextChangedListener(Watch(R.id.edit6, R.id.edit6C))
        weekCalPair.append(R.id.edit6,Pair(::updateCal6, ::updateWeek6))

        findViewById<EditText>(R.id.edit7).addTextChangedListener(Watch(R.id.edit7, R.id.edit7C))
        weekCalPair.append(R.id.edit7,Pair(::updateCal7, ::updateWeek7))


    }

    //series of callback methods that update a cached version of the week / calender based on which text box is modified
    fun updateCal1 (s : String) {
        GlobalApp.calSession?.wakeUp = s
        //reInit()
    }
        fun updateWeek1 (s : String) {
            GlobalApp.weekSession?.monday = s
            //reInit()
        }

        fun updateCal2 (s : String) {
            GlobalApp.calSession?.morning = s
        }
        fun updateWeek2(s : String) {
            GlobalApp.weekSession?.tuesday = s
        }

        fun updateCal3(s : String) {
            GlobalApp.calSession?.lunchTime = s
        }
        fun updateWeek3(s : String) {
            GlobalApp.weekSession?.wednesday = s
        }

        fun updateCal4(s : String) {
            GlobalApp.calSession?.afternoon = s
        }
        fun updateWeek4(s : String) {
            GlobalApp.weekSession?.thursday = s
        }

        fun updateCal5(s : String) {
            GlobalApp.calSession?.dinnerTime = s
        }
        fun updateWeek5(s : String) {
            GlobalApp.weekSession?.friday = s
        }

        fun updateCal6(s : String) {
            GlobalApp.calSession?.evening = s
        }
        fun updateWeek6(s : String) {
            GlobalApp.weekSession?.saturday = s
        }

        fun updateCal7(s : String) {
            GlobalApp.calSession?.bedTime = s
        }
        fun updateWeek7(s : String) {
            GlobalApp.weekSession?.sunday = s
        }


    // class that handles when a text box i modified
    inner class Watch(private val rId: Int, private val rCId: Int) : TextWatcher {

        //once the text has been modified, find the relevant weekCalPair entry based on the id of the View, and call the needed callback based on calender setting
        override fun afterTextChanged(s: Editable) {
            val pairVal = weekCalPair.get(rId)
            val thisT = findViewById<EditText>(rId)

            if (GlobalApp.calSw) //if day
                pairVal.second(s.toString())
            else
                pairVal.first(s.toString())

            thisT.setOnLongClickListener {
                t2s(thisT.text.toString(), tts)
                true
            }

            //redraw
            findViewById<CardView>(rCId).invalidate()
            findViewById<TableLayout>(R.id.calTable).invalidate()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    //class to asynchronously fetch a calender object from the DB for a given user
    inner class GetCalenderFromDB(val name: String) : AsyncTask<String, Int, Calender>() {
        private val db = UserDataBase.getDatabase(this@CalenderActivity).userDataDao()
        override fun doInBackground(vararg params: String?): Calender {
            val f = db.getCalenderByName(name)
            GlobalApp.calSession = f

            return f!!
        }

    }

    //class to asynchronously fetch a week object from the DB for a given user
    inner class GetWeekFromDB(val name: String) : AsyncTask<String, Int, Week>() {
        private val db = UserDataBase.getDatabase(this@CalenderActivity).userDataDao()
        override fun doInBackground(vararg params: String?): Week {
            val f = db.getWeekByName(name)
            GlobalApp.weekSession = f

            return f!!
        }

    }

    //class to asynchronously write a calender object to the DB for a given user
    inner class WriteCalToDB(val name: String, private val c: Calender) : AsyncTask<String, Int, Unit>() {
        private val db = UserDataBase.getDatabase(this@CalenderActivity).userDataDao()

        override fun doInBackground(vararg params: String?) {
            return db.updateCalenderByUser(
                c.wakeUp,
                c.wakeUp_pic_path,
                c.morning,
                c.morning_pic_path,
                c.lunchTime,
                c.lunchTime_pic_path,
                c.afternoon,
                c.afternoon_pic_path,
                c.evening,
                c.evening_pic_path,
                c.dinnerTime,
                c.dinnerTime_pic_path,
                c.bedTime,
                c.bedTime_pic_path,
                name
            )
        }
    }

    //class to asynchronously write a week object to the DB for a given user
    inner class WriteWeekToDB(val name: String, private val w: Week) : AsyncTask<String, Int, Unit>() {
        private val db = UserDataBase.getDatabase(this@CalenderActivity).userDataDao()

        override fun doInBackground(vararg params: String?) {
            return db.updateWeekByUser(
                w.monday,
                w.monday_pic_path,
                w.tuesday,
                w.tuesday_pic_path,
                w.wednesday,
                w.wednesday_pic_path,
                w.thursday,
                w.thursday_pic_path,
                w.friday,
                w.friday_pic_path,
                w.saturday,
                w.saturday_pic_path,
                w.sunday,
                w.sunday_pic_path,
                name
            )
        }


    }

}
