
package com.example.healthappointmentmanagementsystem.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.healthappointmentmanagementsystem.data.dao.AppointmentDao
import com.example.healthappointmentmanagementsystem.data.entities.Appointment

@Database(
    entities = [Appointment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "health_appointment_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
