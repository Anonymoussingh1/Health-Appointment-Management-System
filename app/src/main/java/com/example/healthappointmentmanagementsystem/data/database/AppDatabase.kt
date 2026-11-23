package com.example.healthappointmentmanagementsystem.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.healthappointmentmanagementsystem.data.dao.AppointmentDao
import com.example.healthappointmentmanagementsystem.data.dao.UserDao
import com.example.healthappointmentmanagementsystem.data.entities.Appointment
import com.example.healthappointmentmanagementsystem.data.entities.User

@Database(
    entities = [Appointment::class, User::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao
    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "health_appointment_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}