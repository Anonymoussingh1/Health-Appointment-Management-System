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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.data.IntakeDirectory
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

class PatientHistoryFragment : Fragment(R.layout.fragment_patient_history) {

    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view.findViewById<Spinner>(R.id.spinnerPatientHistory)
        val recordText = view.findViewById<TextView>(R.id.textPatientRecord)

        val doctorUser = authViewModel.currentUser.value
        val doctorName = (doctorUser?.name ?: "Doctor").substringBefore("@").trim().lowercase()

        var myPatients: List<String> = emptyList()

        fun loadPatientRecord(patientName: String) {
            viewLifecycleOwner.lifecycleScope.launch {
                val record = IntakeDirectory.getLatestForPatient(requireContext(), patientName)
                if (record != null) {
                    recordText.text = "• Patient Name: $patientName\n" +
                            "• Chief Complaint / Symptoms: ${record.symptoms}\n" +
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
        }

        appointmentViewModel.appointments.observe(viewLifecycleOwner) { allAppts ->
            val mine = allAppts.filter { it.doctorName.lowercase().contains(doctorName) }
            myPatients = mine.map { it.patientName }.distinct()

            val displayNames = if (myPatients.isEmpty()) {
                listOf("No patients have booked appointments with you yet")
            } else {
                myPatients
            }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, displayNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            if (myPatients.isNotEmpty()) {
                loadPatientRecord(myPatients[0])
            } else {
                recordText.text = "No patients assigned to your schedule yet."
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0 && position < myPatients.size) {
                    loadPatientRecord(myPatients[position])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        view.findViewById<Button>(R.id.btnViewFullRecord).setOnClickListener {
            if (myPatients.isEmpty()) {
                Toast.makeText(requireContext(), "No patients available.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selected = myPatients[spinner.selectedItemPosition]
            Toast.makeText(requireContext(), "Full clinical record for $selected loaded.", Toast.LENGTH_LONG).show()
        }
    }
}
