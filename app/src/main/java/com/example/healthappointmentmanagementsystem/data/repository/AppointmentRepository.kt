package com.example.healthappointmentmanagementsystem.data.repository

import androidx.lifecycle.LiveData
import com.example.healthappointmentmanagementsystem.data.dao.AppointmentDao
import com.example.healthappointmentmanagementsystem.data.entities.Appointment

// THE SINGLE DOOR into the data layer.
// The ViewModel only talks to this class, never to the DAO or the database
// directly. If the group ever swaps Room for something else (a web API, for
// example), this is the only file that changes - no screen moves.
class AppointmentRepository(private val appointmentDao: AppointmentDao) {

    // Live list, kept up to date by Room: when a row changes, every screen
    // observing it is told. Not suspend, because it returns the stream
    // itself rather than a one-off answer.
    fun observeAll(): LiveData<List<Appointment>> =
        appointmentDao.observeAll()

    // suspend: the rest can pause while Room works on a background thread,
    // so the screen never freezes. They must be called from a coroutine
    // (see viewModelScope.launch in AppointmentViewModel).

    // A one-off read of every appointment
    suspend fun getAllAppointments(): List<Appointment> =
        appointmentDao.getAllAppointments()

    // One appointment, or null when that id is not in the table
    suspend fun getAppointmentById(id: String): Appointment? =
        appointmentDao.getAppointmentById(id)

    // How many live bookings a doctor already has for that date and time.
    // Used to stop two patients taking the same slot.
    suspend fun countTaken(doctor: String, date: String, time: String): Int =
        appointmentDao.countTaken(doctor, date, time)

    // Save a new appointment
    suspend fun addAppointment(appointment: Appointment) {
        appointmentDao.insertAppointment(appointment)
    }

    // Change only the status, for confirming or cancelling
    suspend fun updateStatus(id: String, status: String) {
        appointmentDao.updateStatus(id, status)
    }

    // Save changes to an appointment that already exists, matched on its id
    suspend fun updateAppointment(appointment: Appointment) {
        appointmentDao.updateAppointment(appointment)
    }

    // Remove an appointment by id
    suspend fun deleteById(id: String) {
        appointmentDao.deleteById(id)
    }

    // Remove an appointment we already hold
    suspend fun deleteAppointment(appointment: Appointment) {
        appointmentDao.deleteAppointment(appointment)
    }
}
