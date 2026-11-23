package com.example.healthappointmentmanagementsystem.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.ui.admin.AdminHomeFragment
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.LoginFragment
import com.example.healthappointmentmanagementsystem.ui.auth.RegisterFragment
import com.example.healthappointmentmanagementsystem.ui.doctor.DoctorHomeFragment
import com.example.healthappointmentmanagementsystem.ui.patient.PatientHomeFragment

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
            val current: Fragment? = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

            if (user == null) {
                if (current !is LoginFragment && current !is RegisterFragment) {
                    showScreen(LoginFragment())
                }
            } else {
                val target: Fragment = when (user.role.uppercase()) {
                    "ADMIN" -> AdminHomeFragment()
                    "DOCTOR" -> DoctorHomeFragment()
                    else -> PatientHomeFragment()
                }
                if (current?.javaClass != target.javaClass) {
                    showScreen(target)
                }
            }
        }
    }

    private fun showScreen(fragment: Fragment) {
        // Clear the back stack so Back can't return to a previous user's screen
        // or to the Register screen after logging in.
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}