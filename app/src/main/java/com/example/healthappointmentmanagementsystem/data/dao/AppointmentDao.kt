package com.example.healthappointmentmanagementsystem.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthappointmentmanagementsystem.data.entities.Appointment

@Dao
interface AppointmentDao {

    @Insert
    suspend fun insertAppointment(appointment: Appointment)

    @Query("SELECT * FROM appointments")
    suspend fun getAllAppointments(): List<Appointment>

    // Live list: every screen updates automatically when an appointment changes.
    @Query("SELECT * FROM appointments ORDER BY date, time")
    fun observeAll(): LiveData<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: String): Appointment?

    // Counts non-cancelled bookings for the same doctor, date and time.
    @Query(
        "SELECT COUNT(*) FROM appointments " +
                "WHERE doctorName = :doctor AND date = :date AND time = :time " +
                "AND status != 'Cancelled'"
    )
    suspend fun countTaken(doctor: String, date: String, time: String): Int

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)
}