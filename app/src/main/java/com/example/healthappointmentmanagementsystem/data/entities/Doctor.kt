
package com.example.healthappointmentmanagementsystem.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class Doctor(

    @PrimaryKey(autoGenerate = true)
    val doctorId: Int = 0,

    val fullName: String,
    val specialization: String,
    val phoneNumber: String,
    val email: String
)
