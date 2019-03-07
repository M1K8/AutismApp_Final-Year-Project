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

private var layoutManager: RecyclerView.LayoutManager? = null
private var adapter: RecyclerView.Adapter<LoginActivity.Companion.RecyclerAdapter.ViewHolder>? = null


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.


        delButt.setOnClickListener {
            val d = DropAll(this).execute()

            d.get()
            Toast.makeText(this, "All Users Dropped", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        }

        email_sign_in_button.setOnClickListener {
            val newUser =
                User(null, new_user.text.toString(), false, false, false, false, "test.png", Calender(), Week())

            val c = CreateNewUser(this).execute(newUser)

            c.get()
            Toast.makeText(this, "User ${newUser.uName}  Created!", Toast.LENGTH_SHORT).show()
            GlobalApp.setLogged(newUser.uName)
            setResult(Activity.RESULT_OK)
            finish()
        }
        val c = CheckSize(this).execute()


        if (c.get() > 0) {
            layoutManager = LinearLayoutManager(this)
            users_list_view.layoutManager = layoutManager

            adapter = RecyclerAdapter(this)
            users_list_view.adapter = adapter
        }
    }

    companion object {
        class GetAllUsers(private val loginActivity: LoginActivity) : AsyncTask<Void, Int, List<User>>() {
            val db = UserDataBase.getDatabase(loginActivity, null).userDataDao()
            override fun doInBackground(vararg params: Void?): List<User> {
                return db.getAll()
            }
        }

        class DropAll(private val loginActivity: LoginActivity) : AsyncTask<Void, Int, Unit>() {
            val db = UserDataBase.getDatabase(loginActivity, null).userDataDao()
            override fun doInBackground(vararg params: Void?) {
                return db.deleteAll()

            }
        }

        class CreateNewUser(private val loginActivity: LoginActivity) : AsyncTask<User, Int, Unit>() {
            val db = UserDataBase.getDatabase(loginActivity, null).userDataDao()
            override fun doInBackground(vararg params: User) {
                return db.insert(params[0])

            }
        }

        class CheckSize(private val loginActivity: LoginActivity) : AsyncTask<Void, Int, Int>() {
            val db = UserDataBase.getDatabase(loginActivity, null).userDataDao()
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
