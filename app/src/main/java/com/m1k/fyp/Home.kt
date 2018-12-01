package com.m1k.fyp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Opening Activity...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }
    }

}
