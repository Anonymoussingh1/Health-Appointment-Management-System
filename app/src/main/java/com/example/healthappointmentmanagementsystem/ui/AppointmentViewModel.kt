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
            val reasonText = if (!appointment.reason.isNullOrEmpty()) " | Issue: ${appointment.reason}" else ""
            "${appointment.date} at ${appointment.time} | Dr. ${appointment.doctorName} - Patient: ${appointment.patientName}$reasonText [${appointment.status}]"
        }
    }

    init {
        loadSampleAppointments()
    }

    fun loadSampleAppointments() {
        val sampleData = listOf(
            Appointment("1", "Aman", "Lee", "2025-03-30", "10:00 AM", "Confirmed", "Back pain & stiffness"),
            Appointment("2", "Moses Bill", "Somo", "2025-03-30", "11:30 AM", "Pending", "Blurred vision"),
            Appointment("3", "Alia Baba", "John", "2025-03-31", "02:00 PM", "Confirmed", "Annual checkup"),
            Appointment("4", "Gift Shasa", "Sarah", "2025-04-01", "09:15 AM", "Cancelled", "Migraine")
        )
        _appointments.value = sampleData
    }

    fun addAppointment(appointment: Appointment) {
        val currentList = _appointments.value.orEmpty().toMutableList()
        currentList.add(appointment)
        _appointments.value = currentList
    }

    fun addAppointment(appointment: Appointment, onResult: (String?) -> Unit) {
        addAppointment(appointment)
        onResult(null)
    }

    fun cancelAppointment(id: String) {
        val currentList = _appointments.value.orEmpty().map {
            if (it.id == id) it.copy(status = "Cancelled") else it
        }
        _appointments.value = currentList
    }

    fun deleteAppointment(id: String) {
        val currentList = _appointments.value.orEmpty().filter { it.id != id }
        _appointments.value = currentList
    }
}
