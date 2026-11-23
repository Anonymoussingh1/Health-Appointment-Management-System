package com.example.healthappointmentmanagementsystem.data.entities

data class User(
    val userId: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String, // "PATIENT", "DOCTOR" or "ADMIN"
    val hospital: String? = null,
    val specialty: String? = null
)
