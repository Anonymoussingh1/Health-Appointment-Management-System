package com.example.healthappointmentmanagementsystem.data

import android.content.Context

object IntakeDirectory {

    data class IntakeRecord(
        val patientName: String,
        val symptoms: String,
        val allergies: String,
        val medications: String,
        val date: String
    )

    private val storage = mutableMapOf<String, IntakeRecord>()

    fun save(context: Context, record: IntakeRecord) {
        storage[record.patientName.trim().lowercase()] = record
    }

    fun getLatestForPatient(context: Context, patientName: String): IntakeRecord? {
        return storage[patientName.trim().lowercase()]
    }
}
