package com.m1k.fyp

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.widget.*
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.


        email_sign_in_button.setOnClickListener {
            val newUser =
                User(null, new_user.text.toString(), false, false, false, false, "test.png", Calender(), Week())

            val c = CreateNewUser().execute(newUser)

            c.get()
            Toast.makeText(this, "User " + newUser.uName + " Created!", Toast.LENGTH_SHORT).show()
            finish()
        }
        val c = CheckSize().execute()

        if (c.get() > 0) {

            val u = GetAllUsers().execute()


            //2 per room...
            var isFirstRow = true

            //create stopga
            ////

            val layoutNeeded = findViewById<RelativeLayout>(R.id.users_list_view)
            val newTable = TableLayout(this)

            val userNamePicList = u.get()
            var newRow: TableRow? = null

            for (a: User? in userNamePicList!!) {
                var picV: ImageView?
                //populate ting
                if (isFirstRow) {
                    newRow = TableRow(this)

                    /*val spacerView = LayoutInflater.from(this).inflate(R.layout.spacer,null, false)

                    newRow.addView(spacerView)*/
                }

                val textV = TextView(this)
                textV.text = a!!.uName

                val rL = RelativeLayout(this)

                val card = CardView(this)
                rL.addView(textV)

                if (a.picPath != null) {
                    picV = ImageView(this)
                    picV.setImageResource(R.drawable.test)
                    //picV.setImageBitmap(BitmapFactory.decodeFile(File(filesDir,"test.png").absolutePath))
                    rL.addView(picV)
                }

                card.addView(rL)
                newRow?.addView(card)
                if (isFirstRow)
                    newTable.addView(newRow)

                isFirstRow = !isFirstRow
                card.setOnClickListener {
                    GlobalApp.setLogged(a.uName)
                    Toast.makeText(this, "User " + a.uName + " logged in!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }

            }

            layoutNeeded.addView(newTable)
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    inner class GetAllUsers : AsyncTask<Void, Int, List<User>>() {
        val db = UserDataBase.getDatabase(this@LoginActivity, null).userDataDao()
        override fun doInBackground(vararg params: Void?): List<User> {
            return db.getAll()
        }

    }

    inner class CreateNewUser : AsyncTask<User, Int, Unit>() {
        val db = UserDataBase.getDatabase(this@LoginActivity, null).userDataDao()
        override fun doInBackground(vararg params: User) {
            return db.insert(params[0])

        }

    }

    inner class CheckSize : AsyncTask<Void, Int, Int>() {
        val db = UserDataBase.getDatabase(this@LoginActivity, null).userDataDao()
        override fun doInBackground(vararg params: Void?): Int {
            return db.getCount()
        }

    }

}
