package com.example.healthappointmentmanagementsystem.data.entities

data class Appointment(
    val id: String,
    val patientName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val status: String,
    val reason: String? = null
)
