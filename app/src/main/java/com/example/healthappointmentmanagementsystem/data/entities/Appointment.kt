
package com.example.healthappointmentmanagementsystem.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(

    @PrimaryKey
    val id: String,

    val patientName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val status: String
)
