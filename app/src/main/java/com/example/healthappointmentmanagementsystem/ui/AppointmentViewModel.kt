package com.example.healthappointmentmanagementsystem.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.healthappointmentmanagementsystem.data.Appointment
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import kotlinx.coroutines.launch
import com.example.healthappointmentmanagementsystem.data.entities.Appointment as AppointmentEntity

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).appointmentDao()

    val appointments: LiveData<List<Appointment>> =
        dao.observeAll().map { list -> list.map { it.toModel() } }

    val totalAppointmentCount: LiveData<Int> = appointments.map { list ->
        list.size
    }

    val confirmedCount: LiveData<Int> = appointments.map { list ->
        list.count { it.status.equals("Confirmed", ignoreCase = true) }
    }

    val formattedAppointmentList: LiveData<List<String>> = appointments.map { list ->
        list.map { appointment ->
            " ${appointment.date} at ${appointment.time} | Dr. ${appointment.doctorName} - Patient: ${appointment.patientName} [${appointment.status}]"
        }
    }

    // onDone receives an error message, or null when the appointment was saved.
    fun addAppointment(appointment: Appointment, onDone: (String?) -> Unit = {}) {
        viewModelScope.launch {
            val taken = dao.countTaken(
                appointment.doctorName,
                appointment.date,
                appointment.time
            )
            if (taken > 0) {
                onDone("That doctor is already booked at this time. Choose another slot.")
                return@launch
            }
            dao.insertAppointment(appointment.toEntity())
            onDone(null)
        }
    }

    fun cancelAppointment(id: String) {
        viewModelScope.launch { dao.updateStatus(id, "Cancelled") }
    }

    fun deleteAppointment(id: String) {
        viewModelScope.launch { dao.deleteById(id) }
    }

    private fun AppointmentEntity.toModel() = Appointment(
        id = id,
        patientName = patientName,
        doctorName = doctorName,
        date = date,
        time = time,
        status = status
    )

    private fun Appointment.toEntity() = AppointmentEntity(
        id = id,
        patientName = patientName,
        doctorName = doctorName,
        date = date,
        time = time,
        status = status
    )
}