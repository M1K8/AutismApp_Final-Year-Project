package com.m1k.fyp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private var db: UserDataBase? = null
    private lateinit var dbH : DbWorkerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        dbH = DbWorkerThread("DBWorker")
        dbH.start()

        db = UserDataBase.getInstance(this)

/*        fab.setOnClickListener { view ->
            Snackbar.make(view, "Opening Activity...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }*/


        val s = findViewById<Button>(R.id.loginButton)

        s.x = 50f
        s.y = 1550f

        s.setOnClickListener{ view ->
            Snackbar.make(view, "Opening Activity...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }

    }

    private fun addDataToDB(userTable: UserTable) {
        val task = Runnable { db?.userDataDao()?.insert(userTable) }
        dbH.postTask(task)
    }

}
