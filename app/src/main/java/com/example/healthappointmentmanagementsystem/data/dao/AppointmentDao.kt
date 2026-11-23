
package com.example.healthappointmentmanagementsystem.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.healthappointmentmanagementsystem.data.entities.Appointment

@Dao
interface AppointmentDao {

    @Insert
    suspend fun insertAppointment(appointment: Appointment)

    @Query("SELECT * FROM appointments")
    suspend fun getAllAppointments(): List<Appointment>

    @Query("SELECT * FROM appointments WHERE appointmentId = :id")
    suspend fun getAppointmentById(id: Int): Appointment?

    @Query("SELECT * FROM appointments WHERE doctorId = :doctorId")
    suspend fun getAppointmentsByDoctor(doctorId: Int): List<Appointment>

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)
}
