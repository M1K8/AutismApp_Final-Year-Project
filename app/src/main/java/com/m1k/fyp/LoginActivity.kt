package com.m1k.fyp

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File

private var layoutManager: RecyclerView.LayoutManager? = null
private var adapter: RecyclerView.Adapter<LoginActivity.Companion.RecyclerAdapter.ViewHolder>? = null


class LoginActivity : AppCompatActivity() {

    override fun onResume() {
        if (GlobalApp.vib) {
            findViewById<View>(R.id.login_form).setOnTouchListener { v, event ->
                GlobalApp.vibrate(20, v.context)
                super.onTouchEvent(event)
            }
        }
        super.onResume()

        if (!GlobalApp.isLogged())
            delButt.visibility = View.GONE
        else
            delButt.visibility = View.VISIBLE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //create our dialog to confirm is user wants to delete
        val alertCL = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val delU = GlobalApp.getLogged()
                    val uProm = GetUser(this).execute(GlobalApp.getLogged())
                    val u = uProm.get()

                    if (u != null) {
                        val userPath = "${this.getExternalFilesDir("")}/$delU"

                        //delete the folder and all its children
                        File(userPath).deleteRecursively()
                    }

                    //remove the user from the DB
                    val d = Drop(this).execute(delU)

                    d.get()

                    //..and clean up, return to the home screen
                    GlobalApp.logOut()
                    Toast.makeText(this, "User $delU Deleted", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                else -> {
                    dialog.dismiss()
                }
            }
        }

        //define the behaviour of all the buttons

        delButt.setOnClickListener {
            val delU = GlobalApp.getLogged()
            val alertD = AlertDialog.Builder(this)

            alertD.setMessage("Deleting Account $delU. This will also delete all saved images. Are you sure?").apply {
                setPositiveButton("Yes", alertCL)
                setNegativeButton("No", alertCL)
                show()
            }


        }

        email_sign_in_button.setOnClickListener {
            if (new_user.text.toString().isBlank() || new_user.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter a non empty name", Toast.LENGTH_LONG).show()
                new_user.text.clear()

            } else {
                val newUser =
                    User( new_user.text.toString(), false, false, false, false, "test.png", Calender(), Week())


                val c = CreateNewUser(this).execute(newUser)
                if (c.get().compareTo(-1) == 0)
                {
                    Toast.makeText(this, "Username exists, please enter a different username!", Toast.LENGTH_SHORT).show()
                    new_user.text.clear()
                } else {
                    Toast.makeText(this, "User ${newUser.uName}  Created!", Toast.LENGTH_SHORT).show()
                    GlobalApp.setLogged(newUser.uName)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }


        val ch = CheckSize(this).execute()
        //if there are users, draw them
        if (ch.get() > 0) {
            layoutManager = LinearLayoutManager(this)
            users_list_view.layoutManager = layoutManager

            adapter = RecyclerAdapter(this)
            users_list_view.adapter = adapter
        }
    }

    //series of helper classes to read / write from the DB
    companion object {

        class GetAllUsers(loginActivity: LoginActivity) : AsyncTask<Void, Int, List<User>>() {
            private val db = UserDataBase.getDatabase(loginActivity).userDataDao()
            override fun doInBackground(vararg params: Void?): List<User> {
                return db.getAll()
            }
        }

        class GetUser(loginActivity: LoginActivity) : AsyncTask<String, Int, User>() {
            private val db = UserDataBase.getDatabase(loginActivity).userDataDao()
            override fun doInBackground(vararg params: String): User? {
                return db.getByUserName(params[0])
            }
        }

        class Drop(loginActivity: LoginActivity) : AsyncTask<String, Int, Unit>() {
            private val db = UserDataBase.getDatabase(loginActivity).userDataDao()
            override fun doInBackground(vararg params: String) {
                return db.deleteUser(params[0])

            }
        }

        class CreateNewUser(loginActivity: LoginActivity) : AsyncTask<User, Int, Long>() {
            private val db = UserDataBase.getDatabase(loginActivity).userDataDao()
            override fun doInBackground(vararg params: User) : Long{
                return db.insert(params[0])

            }
        }

        class CheckSize(loginActivity: LoginActivity) : AsyncTask<Void, Int, Int>() {
            private val db = UserDataBase.getDatabase(loginActivity).userDataDao()
            override fun doInBackground(vararg params: Void?): Int {
                return db.getCount()
            }
        }


        class RecyclerAdapter(private val loginActivity: LoginActivity) :
            RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
            var users: List<User>? = null
            init {
                val e = GetAllUsers(loginActivity).execute()
                users = e.get()
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
                viewHolder.userImage.setImageResource(R.drawable.test)
                viewHolder.userName.text = users!![i].uName
                viewHolder.userName.textSize = 48f
            }

            override fun getItemCount(): Int {
                return users!!.size
            }

            override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
                val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.card_layout, viewGroup, false)
                return ViewHolder(v)
            }

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

                var userImage: ImageView = itemView.findViewById(R.id.user_image)
                var userName: TextView = itemView.findViewById(R.id.user_name)

                init {
                    itemView.setOnClickListener {

                        val txt = this@RecyclerAdapter.users!![adapterPosition].uName
                        GlobalApp.setLogged(txt)

                        Toast.makeText(
                            loginActivity,
                            "User $txt Logged In!",
                            Toast.LENGTH_SHORT
                        ).show()
                        loginActivity.setResult(Activity.RESULT_OK)
                        loginActivity.finish()
                    }
                }
            }
        }
    }
}
