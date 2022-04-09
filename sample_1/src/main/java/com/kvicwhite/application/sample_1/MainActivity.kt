package com.kvicwhite.application.sample_1

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kvicwhite.application.sample_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val username = "whoami"
        val password = "admin0000"
        setContentView(binding.root)

        binding.signUpButton.isEnabled = false
        // sets sign up button as false by default

        binding.signInButton.setOnClickListener {
            val inputUsername = binding.usernameEditText.text.toString()
            val inputPassword = binding.passwordEditText.text.toString()
            val coarseLocationPermission =
                ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION")
            val fineLocationPermission =
                ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")

            if (inputUsername == username && inputPassword == password) {
                val eventHandler = DialogInterface.OnClickListener { _, p1 ->
                    if (p1 == DialogInterface.BUTTON_POSITIVE) {
                        if (fineLocationPermission == PackageManager.PERMISSION_DENIED
                            && coarseLocationPermission == PackageManager.PERMISSION_DENIED) {

                            val requestPermissionLauncher = registerForActivityResult(
                                ActivityResultContracts.RequestPermission()
                            ) { isGranted ->
                                if (isGranted) {
                                    Toast.makeText(this@MainActivity, "Permission has been granted", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MainActivity, "Permission is necessary", Toast.LENGTH_SHORT).show()
                                }
                            }

                            requestPermissionLauncher.launch("android.permission.ACCESS_FINE_LOCATION")
                        }
                    } else if (p1 == DialogInterface.BUTTON_NEGATIVE) {
                        Toast.makeText(this@MainActivity, "Ok cool", Toast.LENGTH_SHORT).show()
                    }
                }


                AlertDialog.Builder(this).run {
                    setTitle("Ask Permission")
                    setIcon(android.R.drawable.ic_dialog_alert)
                    setMessage("Please grant following permission")
                    setPositiveButton("OK", eventHandler)
                    setNegativeButton("Cancel", eventHandler)

                    show()
                }
            }
        }

        binding.signUpButton.setOnClickListener {

        }
    }
}