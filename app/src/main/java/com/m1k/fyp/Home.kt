package com.m1k.fyp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_home.*

class Home : AppCompatActivity() {

    private var db: UserDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //setSupportActionBar(toolbar)

        db = UserDataBase.getDatabase(this)

         loginButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}