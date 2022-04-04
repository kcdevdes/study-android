package com.kvicwhite.application.ch7_layout

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "This is an example presenting how to use Layout Class!", Toast.LENGTH_LONG).show()
    }
}