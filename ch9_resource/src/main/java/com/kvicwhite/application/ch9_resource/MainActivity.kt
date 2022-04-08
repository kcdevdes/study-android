package com.kvicwhite.application.ch9_resource

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kvicwhite.application.ch9_resource.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}