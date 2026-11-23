package com.example.healthappointmentmanagementsystem.data.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healthappointmentmanagementsystem.data.entities.Appointment

class AppointmentDao(private val context: Context) {
    suspend fun insertAppointment(appointment: Appointment) {}
    suspend fun getAllAppointments(): List<Appointment> = emptyList()
    fun observeAll(): LiveData<List<Appointment>> = MutableLiveData(emptyList())
    suspend fun getAppointmentById(id: String): Appointment? = null
    suspend fun countTaken(doctor: String, date: String, time: String): Int = 0
    suspend fun updateStatus(id: String, status: String) {}
    suspend fun deleteById(id: String) {}
    suspend fun updateAppointment(appointment: Appointment) {}
    suspend fun deleteAppointment(appointment: Appointment) {}
}
