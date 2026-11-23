package com.example.healthappointmentmanagementsystem.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.healthappointmentmanagementsystem.data.Appointment

class AppointmentViewModel : ViewModel() {

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> get() = _appointments

    val totalAppointmentCount: LiveData<Int> = _appointments.map { list ->
        list.size
    }

    val confirmedCount: LiveData<Int> = _appointments.map { list ->
        list.count { it.status.equals("Confirmed", ignoreCase = true) }
    }

    val formattedAppointmentList: LiveData<List<String>> = _appointments.map { list ->
        list.map { appointment ->
            " ${appointment.date} at ${appointment.time} | Dr. ${appointment.doctorName} - Patient: ${appointment.patientName} [${appointment.status}]"
        }
    }

    init {
        loadSampleAppointments()
    }

    fun loadSampleAppointments() {
        val sampleData = listOf(
            Appointment("1", "Aman", "Lee", "2025-03-30", "10:00 AM", "Confirmed"),
            Appointment("2", "Moses Bill", "Somo", "2025-03-30", "11:30 AM", "Pending"),
            Appointment("3", "Alia Baba", "John", "2025-03-31", "02:00 PM", "Confirmed"),
            Appointment("4", "Gift Shasa", "Sarah", "2025-04-01", "09:15 AM", "Cancelled")
        )
        _appointments.value = sampleData
    }

    fun addAppointment(appointment: Appointment) {
        val currentList = _appointments.value.orEmpty().toMutableList()
        currentList.add(appointment)
        _appointments.value = currentList
    }
}