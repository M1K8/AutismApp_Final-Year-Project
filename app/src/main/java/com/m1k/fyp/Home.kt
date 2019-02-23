package com.m1k.fyp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.content_home.*

class Home : AppCompatActivity() {

    var db: UserDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //setSupportActionBar(toolbar)

        db = UserDataBase.getDatabase(this)

        var test : UserTable = UserTable(null, "e", true,true,true,"")

        db?.userDataDao()?.insert(test)

        var test2 = db?.userDataDao()?.getByUserName("e")

        //check DB works properly...

        if (test2 != null)
            Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this,"o no", Toast.LENGTH_SHORT).show()


         loginButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        camButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        drawButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, DrawingActivity::class.java)
            startActivity(intent)
        }


        settingsButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }

    }
}