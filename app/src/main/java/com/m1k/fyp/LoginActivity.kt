package com.m1k.fyp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.
        email_sign_in_button.setOnClickListener { attemptLogin() }
        val c = CheckSize().execute()

        if (c.get() > 0) {

            val u = GetAllUsers().execute()


            //2 per room...
            var isFirstRow = true

            //create stopgap
            val spacerView = View(this)
            val param = LinearLayout.LayoutParams(0, 0, 1f)

            spacerView.layoutParams = param
            ////

            val layoutNeeded = findViewById<RelativeLayout>(R.id.users_list_view)
            val newTable = TableLayout(this)

            val userNamePicList = u.get()

            for (a: NamePicPair in userNamePicList) {
                var newRow: TableRow? = null
                var picV: ImageView?
                //populate ting
                if (isFirstRow) {
                    newRow = TableRow(this)
                    newRow.addView(spacerView)
                }

                val textV = TextView(this)
                textV.text = a.uName

                val card = CardView(this)
                card.addView(textV)

                if (a.picPath != null) {
                    picV = ImageView(this)
                    picV.setImageBitmap(BitmapFactory.decodeFile(a.picPath))
                    card.addView(picV)
                }





                newRow?.addView(card)
                newTable.addView(newRow)

                isFirstRow = !isFirstRow
            }

            layoutNeeded.addView(newTable)
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        email.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()

        var cancel = false
        var focusView: View? = null


        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthTask = UserLoginTask(emailStr)
            mAuthTask!!.execute(emailStr)
        }
    }

    private fun isEmailValid(email: String?): Boolean {
        return email != null
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 0 else 1).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    login_form.visibility = if (show) View.GONE else View.VISIBLE
                }
            })
        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    login_progress.visibility = if (show) View.VISIBLE else View.GONE
                }
            }
            )
    }

    inner class GetAllUsers : AsyncTask<Void, Int, List<NamePicPair>>() {
        val db = UserDataBase.getDatabase(this@LoginActivity, null).userDataDao()
        override fun doInBackground(vararg params: Void?): List<NamePicPair>? {
            return db.getAllNames()
        }

    }

    inner class CheckSize : AsyncTask<Void, Int, Int>() {
        val db = UserDataBase.getDatabase(this@LoginActivity, null).userDataDao()
        override fun doInBackground(vararg params: Void?): Int {
            return db.getCount()
        }

    }

    inner class UserLoginTask internal constructor(s: String) : AsyncTask<String, Void, User?>() {

        val name = s
        val db = UserDataBase.getDatabase(this@LoginActivity, null).userDataDao()

        override fun doInBackground(vararg params: String?): User? {
            return db.getByUserName(name)
        }

        override fun onPostExecute(success: User?) {
            mAuthTask = null
            showProgress(false)

            if (success != null) {
                finish()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

}
