package com.example.healthappointmentmanagementsystem.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.ui.appointments.AppointmentListFragment
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.LoginFragment

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
        }

        authViewModel.currentUser.observe(this) { user ->
            val current = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (user != null && current !is AppointmentListFragment) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, AppointmentListFragment())
                    .commit()
            }
        }
    }
}