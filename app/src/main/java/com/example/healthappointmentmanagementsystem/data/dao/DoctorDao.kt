
package com.example.healthappointmentmanagementsystem.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.healthappointmentmanagementsystem.data.entities.Doctor

@Dao
interface DoctorDao {

    @Insert
    suspend fun insertDoctor(doctor: Doctor)

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<Doctor>

    @Query("SELECT * FROM doctors WHERE doctorId = :id")
    suspend fun getDoctorById(id: Int): Doctor?

    @Update
    suspend fun updateDoctor(doctor: Doctor)

    @Delete
    suspend fun deleteDoctor(doctor: Doctor)
}
