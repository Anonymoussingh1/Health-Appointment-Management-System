package com.example.healthappointmentmanagementsystem.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healthappointmentmanagementsystem.R

class MainActivity : AppCompatActivity() {

    private val viewModel: AppointmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()
    }

    private fun setupObservers() {
        val tvTotalCount = findViewById<TextView>(R.id.tvTotalCount)
        val tvConfirmedCount = findViewById<TextView>(R.id.tvConfirmedCount)
        val tvAppointmentList = findViewById<TextView>(R.id.tvAppointmentList)

        viewModel.totalAppointmentCount.observe(this) { count ->
            tvTotalCount.text = "Total Appointments: $count"
        }

        viewModel.confirmedCount.observe(this) { confirmedCount ->
            tvConfirmedCount.text = "Confirmed Appointments: $confirmedCount"
        }

        viewModel.formattedAppointmentList.observe(this) { formattedList ->
            tvAppointmentList.text = formattedList.joinToString(separator = "\n\n")
        }
    }
}
