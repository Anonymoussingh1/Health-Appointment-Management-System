package com.example.healthappointmentmanagementsystem.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import com.example.healthappointmentmanagementsystem.data.entities.Appointment
import com.example.healthappointmentmanagementsystem.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

// AndroidViewModel: a ViewModel that receives the Application.
// Opening a Room database needs a Context, and a ViewModel must never hold a
// screen's Context (the screen dies on rotation, the ViewModel does not).
// The Application is safe, because it lives as long as the app itself.
class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    // Build the data layer once: database -> DAO -> repository.
    // From here on the ViewModel never mentions Room again.
    private val repository = AppointmentRepository(
        AppDatabase.getDatabase(application).appointmentDao()
    )

    // ── 1. THE DATA ──
    // Was a hard-coded list in memory; now it is whatever Room holds.
    private val _appointments = MutableLiveData<List<Appointment>>(emptyList())
    val appointments: LiveData<List<Appointment>> get() = _appointments

    // ── 2. TRANSFORMATIONS (unchanged from Stage 2) ──
    // The screens bind to these, so none of them had to change.
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

    // ── 3. ACTIONS ──
    // Same signature the screens already call. What changed is underneath:
    // the row goes into Room instead of into a list in memory.
    fun addAppointment(appointment: Appointment) {
        // COROUTINE: save in the background so the screen never freezes.
        // viewModelScope is cancelled automatically when the ViewModel is
        // cleared, so no database work leaks after the user leaves.
        viewModelScope.launch {
            repository.addAppointment(appointment)
            loadAppointments()
        }
    }

    // Confirm or cancel an appointment that already exists
    fun updateAppointmentStatus(appointment: Appointment, newStatus: String) {
        viewModelScope.launch {
            repository.updateAppointment(appointment.copy(status = newStatus))
            loadAppointments()
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.deleteAppointment(appointment)
            loadAppointments()
        }
    }

    // Read the table again, for a pull-to-refresh or after returning to a screen
    fun refresh() {
        viewModelScope.launch { loadAppointments() }
    }

    // ── 4. READING FROM ROOM ──
    // suspend, so it can only run inside a coroutine. Room runs the query on a
    // background thread and resumes here with the rows, which are published to
    // the screens through LiveData.
    private suspend fun loadAppointments() {
        _appointments.value = repository.getAllAppointments()
    }

    // TEMPORARY: the app has no "book appointment" screen yet, so without this
    // the list would always be empty. Writes the Stage 2 sample rows into Room
    // once, on a fresh install only. Delete this when a booking screen exists.
    private suspend fun seedSampleAppointmentsIfEmpty() {
        if (repository.getAllAppointments().isNotEmpty()) return

        val sampleData = listOf(
            Appointment("1", "Aman", "Lee", "2025-03-30", "10:00 AM", "Confirmed"),
            Appointment("2", "Moses Bill", "Somo", "2025-03-30", "11:30 AM", "Pending"),
            Appointment("3", "Alia Baba", "John", "2025-03-31", "02:00 PM", "Confirmed"),
            Appointment("4", "Gift Shasa", "Sarah", "2025-04-01", "09:15 AM", "Cancelled")
        )
        sampleData.forEach { repository.addAppointment(it) }
    }

    // ── 5. LIFECYCLE ──
    init {
        Log.d(TAG, "AppointmentViewModel: created")
        // Load whatever was saved in an earlier run, so the app opens with
        // real data instead of an empty screen.
        viewModelScope.launch {
            seedSampleAppointmentsIfEmpty()
            loadAppointments()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "AppointmentViewModel: onCleared")
    }

    private companion object {
        const val TAG = "HEALTH_LIFECYCLE"
    }
}
