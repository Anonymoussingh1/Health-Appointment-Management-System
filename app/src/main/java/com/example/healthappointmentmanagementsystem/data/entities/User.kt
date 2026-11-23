package com.example.healthappointmentmanagementsystem.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String // "PATIENT", "DOCTOR" or "ADMIN"
)