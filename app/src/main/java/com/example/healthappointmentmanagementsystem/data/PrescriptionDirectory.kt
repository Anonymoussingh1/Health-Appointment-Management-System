package com.example.healthappointmentmanagementsystem.data

import android.content.Context

object PrescriptionDirectory {

    data class RefillRequest(
        val patientName: String,
        val medication: String,
        val date: String
    )

    private val requests = mutableListOf<RefillRequest>()

    fun addRequest(context: Context, request: RefillRequest) {
        requests.add(request)
    }

    fun getRequests(context: Context): List<RefillRequest> {
        return requests.toList()
    }
}
