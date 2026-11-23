package com.example.healthappointmentmanagementsystem.ui.doctor

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.data.IntakeDirectory
import com.example.healthappointmentmanagementsystem.data.UserDirectory
import kotlinx.coroutines.launch

class PatientHistoryFragment : Fragment(R.layout.fragment_patient_history) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view.findViewById<Spinner>(R.id.spinnerPatientHistory)
        val recordText = view.findViewById<TextView>(R.id.textPatientRecord)

        var patientList: List<UserDirectory.Person> = emptyList()

        fun loadPatientRecord(patientName: String) {
            val record = IntakeDirectory.getLatestForPatient(requireContext(), patientName)
            if (record != null) {
                recordText.text = "• Chief Complaint / Symptoms: ${record.symptoms}\n" +
                        "• Known Allergies: ${record.allergies}\n" +
                        "• Current Medications: ${record.medications}\n" +
                        "• Submission Date: ${record.date}\n" +
                        "• Intake Form Status: Completed online"
            } else {
                recordText.text = "• Patient Name: $patientName\n" +
                        "• Intake Form: Not yet submitted by patient\n" +
                        "• Medical History: No prior intake records found."
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            patientList = UserDirectory.byRole(requireContext(), "PATIENT")
            val names = if (patientList.isEmpty()) {
                listOf("No registered patients found")
            } else {
                patientList.map { "${it.name} (${it.email})" }
            }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            if (patientList.isNotEmpty()) {
                loadPatientRecord(patientList[0].name)
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0 && position < patientList.size) {
                    loadPatientRecord(patientList[position].name)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        view.findViewById<Button>(R.id.btnViewFullRecord).setOnClickListener {
            if (patientList.isEmpty()) {
                Toast.makeText(requireContext(), "No patients available.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selected = patientList[spinner.selectedItemPosition]
            Toast.makeText(requireContext(), "Full clinical record for ${selected.name} loaded.", Toast.LENGTH_LONG).show()
        }
    }
}
