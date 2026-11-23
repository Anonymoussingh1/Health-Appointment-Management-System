
package com.example.healthappointmentmanagementsystem.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(

    @PrimaryKey(autoGenerate = true)
    val appointmentId: Int = 0,

    val patientName: String,
    val doctorId: Int,
    val appointmentDate: String,
    val appointmentTime: String,
    val reason: String,
    val status: String = "Pending"
)
