package com.example.healthappointmentmanagementsystem.data

data class Appointment(
    val id: String,
    val patientName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val status: String, // e.g., "Confirmed", "Pending", "Cancelled"
    val reason: String? = null
)
