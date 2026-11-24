package com.example.healthappointmentmanagementsystem.data

import android.content.Context
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object IntakeDirectory {

    data class IntakeRecord(
        val patientName: String,
        val symptoms: String,
        val allergies: String,
        val medications: String,
        val date: String
    )

    suspend fun save(context: Context, record: IntakeRecord) = withContext(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(context).writableDatabase
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS intake_records (id INTEGER PRIMARY KEY AUTOINCREMENT, patientName TEXT, symptoms TEXT, allergies TEXT, medications TEXT, date TEXT)",
            arrayOf()
        )
        db.execSQL(
            "INSERT INTO intake_records (patientName, symptoms, allergies, medications, date) VALUES (?, ?, ?, ?, ?)",
            arrayOf<Any?>(record.patientName, record.symptoms, record.allergies, record.medications, record.date)
        )
    }

    suspend fun getLatestForPatient(context: Context, patientName: String): IntakeRecord? = withContext(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(context).readableDatabase
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS intake_records (id INTEGER PRIMARY KEY AUTOINCREMENT, patientName TEXT, symptoms TEXT, allergies TEXT, medications TEXT, date TEXT)",
            arrayOf()
        )
        db.rawQuery(
            "SELECT patientName, symptoms, allergies, medications, date FROM intake_records WHERE TRIM(LOWER(patientName)) = TRIM(LOWER(?)) ORDER BY id DESC LIMIT 1",
            arrayOf(patientName)
        ).use { cursor ->
            if (cursor.moveToNext()) {
                IntakeRecord(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            } else {
                null
            }
        }
    }
}
