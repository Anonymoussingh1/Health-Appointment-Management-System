package com.example.healthappointmentmanagementsystem.data.repository

import com.example.healthappointmentmanagementsystem.data.dao.AppointmentDao
import com.example.healthappointmentmanagementsystem.data.entities.Appointment

// THE SINGLE DOOR into the data layer.
// The ViewModel only talks to this class, never to the DAO or the database
// directly. If the group ever swaps Room for something else (a web API, for
// example), this is the only file that changes - no screen moves.
class AppointmentRepository(private val appointmentDao: AppointmentDao) {

    // suspend: each of these can pause while Room does the work on a
    // background thread, so the screen never freezes. They must be called
    // from a coroutine (see viewModelScope.launch in AppointmentViewModel).

    // Every appointment that has been saved
    suspend fun getAllAppointments(): List<Appointment> =
        appointmentDao.getAllAppointments()

    // One appointment, or null when that id is not in the table
    suspend fun getAppointmentById(id: String): Appointment? =
        appointmentDao.getAppointmentById(id)

    // Save a new appointment
    suspend fun addAppointment(appointment: Appointment) {
        appointmentDao.insertAppointment(appointment)
    }

    // Save changes to an appointment that already exists, matched on its id
    suspend fun updateAppointment(appointment: Appointment) {
        appointmentDao.updateAppointment(appointment)
    }

    // Remove an appointment from the table
    suspend fun deleteAppointment(appointment: Appointment) {
        appointmentDao.deleteAppointment(appointment)
    }
}
