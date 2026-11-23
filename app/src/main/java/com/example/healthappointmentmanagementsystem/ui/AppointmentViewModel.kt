package com.example.healthappointmentmanagementsystem.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.healthappointmentmanagementsystem.data.Appointment
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import com.example.healthappointmentmanagementsystem.data.repository.AppointmentRepository
import kotlinx.coroutines.launch
import com.example.healthappointmentmanagementsystem.data.entities.Appointment as AppointmentEntity

// AndroidViewModel: a ViewModel that receives the Application.
// Opening a Room database needs a Context, and a ViewModel must never hold a
// screen's Context (the screen dies on rotation, the ViewModel does not).
// The Application is safe, because it lives as long as the app itself.
class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    // Build the data layer once: database -> DAO -> repository.
    // The ViewModel never touches the DAO or the database again; every read
    // and write below goes through this one object.
    private val repository = AppointmentRepository(
        AppDatabase.getDatabase(application).appointmentDao()
    )

    // ── 1. THE DATA ──
    // Room's own live query. When a row is inserted, updated or deleted,
    // Room pushes a new list and every observing screen redraws itself.
    // The entities are mapped to the UI model so no screen handles Room types.
    val appointments: LiveData<List<Appointment>> =
        repository.observeAll().map { list -> list.map { it.toModel() } }

    // ── 2. TRANSFORMATIONS ──
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

    // ── 3. ACTIONS ──
    // onDone receives an error message, or null when the appointment was saved.
    fun addAppointment(appointment: Appointment, onDone: (String?) -> Unit = {}) {
        // COROUTINE: the slot check and the insert both run in the background
        // so the screen never freezes. viewModelScope is cancelled
        // automatically when the ViewModel is cleared.
        viewModelScope.launch {
            val taken = repository.countTaken(
                appointment.doctorName,
                appointment.date,
                appointment.time
            )
            if (taken > 0) {
                onDone("That doctor is already booked at this time. Choose another slot.")
                return@launch
            }
            repository.addAppointment(appointment.toEntity())
            // No manual refresh: observeAll() emits the new list by itself.
            onDone(null)
        }
    }

    fun cancelAppointment(id: String) {
        viewModelScope.launch { repository.updateStatus(id, "Cancelled") }
    }

    fun deleteAppointment(id: String) {
        viewModelScope.launch { repository.deleteById(id) }
    }

    // ── 4. MAPPING between the Room entity and the UI model ──
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
