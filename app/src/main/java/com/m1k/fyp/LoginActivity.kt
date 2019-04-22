package com.m1k.fyp

import android.app.Activity
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
    private fun deleteRecursive(fileOrDirectory: File) {

        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles()) {
                deleteRecursive(child)
            }
        }

        if (GlobalApp.isLogged())
            GlobalApp.logOut()
    }


    override fun onResume() {
        if (GlobalApp.vib) {
            findViewById<View>(R.id.login_form).setOnTouchListener { v, event ->
                GlobalApp.vibrate(20, v.context)
                super.onTouchEvent(event)
            }
        }
        super.onResume()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        delButt.setOnClickListener {

            val uProm = GetAllUsers(this).execute()
            val u = uProm.get()

            for (user in u) {
                val userPath = "${this.getExternalFilesDir("")}/${user.uName}/"

                deleteRecursive(File(userPath))
            }


            val d = DropAll(this).execute()

            d.get()
            Toast.makeText(this, "All Users Dropped", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
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


        if (ch.get() > 0) {
            layoutManager = LinearLayoutManager(this)
            users_list_view.layoutManager = layoutManager

            adapter = RecyclerAdapter(this)
            users_list_view.adapter = adapter
        }
    }


    companion object {
        class GetAllUsers(loginActivity: LoginActivity) : AsyncTask<Void, Int, List<User>>() {
            private val db = UserDataBase.getDatabase(loginActivity).userDataDao()
            override fun doInBackground(vararg params: Void?): List<User> {
                return db.getAll()
            }
        }

        class DropAll(loginActivity: LoginActivity) : AsyncTask<Void, Int, Unit>() {
            private val db = UserDataBase.getDatabase(loginActivity).userDataDao()
            override fun doInBackground(vararg params: Void?) {
                return db.deleteAll()

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
                viewHolder.user_image.setImageResource(R.drawable.test)
                viewHolder.user_name.text = users!![i].uName
                viewHolder.user_name.textSize = 48f
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

                var user_image: ImageView = itemView.findViewById(R.id.user_image)
                var user_name: TextView = itemView.findViewById(R.id.user_name)

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
