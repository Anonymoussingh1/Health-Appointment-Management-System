package com.example.healthappointmentmanagementsystem.ui.doctor

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.data.PrescriptionDirectory
import com.example.healthappointmentmanagementsystem.data.UserDirectory
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter
import kotlinx.coroutines.launch

class DoctorPrescriptionsFragment : Fragment(R.layout.fragment_doctor_prescriptions) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinnerPatients = view.findViewById<Spinner>(R.id.spinnerPatients)
        val editRx = view.findViewById<EditText>(R.id.editRx)
        val recyclerRequests = view.findViewById<RecyclerView>(R.id.recyclerRefillRequests)
        val textNoRequests = view.findViewById<TextView>(R.id.textNoRequests)

        recyclerRequests.layoutManager = LinearLayoutManager(requireContext())

        val requests = PrescriptionDirectory.getRequests(requireContext())
        val requestStrings = requests.map { "Refill Request: ${it.patientName} — ${it.medication} (${it.date})" }

        val requestAdapter = SimpleListAdapter<String>(R.layout.item_simple_row) { row, text ->
            row.findViewById<TextView>(R.id.textTitle).text = text
        }
        recyclerRequests.adapter = requestAdapter
        requestAdapter.submit(requestStrings)
        textNoRequests.visibility = if (requestStrings.isEmpty()) View.VISIBLE else View.GONE

        var patientList: List<UserDirectory.Person> = emptyList()

        viewLifecycleOwner.lifecycleScope.launch {
            patientList = UserDirectory.byRole(requireContext(), "PATIENT")
            val names = if (patientList.isEmpty()) {
                listOf("No registered patients found")
            } else {
                patientList.map { "${it.name} (${it.email})" }
            }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPatients.adapter = adapter
        }

        view.findViewById<Button>(R.id.btnIssueRx).setOnClickListener {
            if (patientList.isEmpty()) {
                Toast.makeText(requireContext(), "No patients available.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedPosition = spinnerPatients.selectedItemPosition
            if (selectedPosition < 0 || selectedPosition >= patientList.size) {
                Toast.makeText(requireContext(), "Please select a valid patient.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val patient = patientList[selectedPosition]
            val rx = editRx.text.toString().trim()
            if (rx.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter prescription details.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(requireContext(), "E-Prescription issued successfully to ${patient.name}!", Toast.LENGTH_LONG).show()
            editRx.setText("")
        }

        view.findViewById<Button>(R.id.btnOrderLabs).setOnClickListener {
            if (patientList.isEmpty()) {
                Toast.makeText(requireContext(), "No patients available.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedPosition = spinnerPatients.selectedItemPosition
            if (selectedPosition < 0 || selectedPosition >= patientList.size) {
                Toast.makeText(requireContext(), "Please select a valid patient.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val patient = patientList[selectedPosition]
            Toast.makeText(requireContext(), "Lab tests & specialist referral ordered for ${patient.name}!", Toast.LENGTH_LONG).show()
        }
    }
}
