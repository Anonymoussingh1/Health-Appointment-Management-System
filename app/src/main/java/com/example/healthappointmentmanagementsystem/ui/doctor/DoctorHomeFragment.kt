package com.example.healthappointmentmanagementsystem.ui.doctor

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter
import com.example.healthappointmentmanagementsystem.ui.consultation.ConsultationFragment

class DoctorHomeFragment : Fragment(R.layout.fragment_doctor_home) {

    private val viewModel: AppointmentViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private var mineCache: List<String> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = authViewModel.currentUser.value
        val name = user?.name ?: "Doctor"
        val cleanName = name.substringBefore("@").trim()

        view.findViewById<TextView>(R.id.textWelcome).text = "Dr. $cleanName"
        view.findViewById<TextView>(R.id.textProfile).text =
            "Name: Dr. $cleanName\nEmail: ${user?.email ?: "-"}\nRole: Doctor${if (!user?.specialty.isNullOrEmpty()) "\nSpecialty: ${user?.specialty}" else ""}${if (!user?.hospital.isNullOrEmpty()) "\nHospital: ${user?.hospital}" else ""}"

        // Doctor Professional Tool Buttons
        view.findViewById<Button>(R.id.btnSchedule).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DoctorScheduleFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnWaitingRoom).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WaitingRoomFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnPatientHistory).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PatientHistoryFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnPrescriptions).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DoctorPrescriptionsFragment())
                .addToBackStack(null)
                .commit()
        }

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerAppointments)
        val empty = view.findViewById<TextView>(R.id.textEmpty)
        val summary = view.findViewById<TextView>(R.id.textSummary)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SimpleListAdapter<String>(R.layout.item_appointment) { row, text ->
            val dateView = row.findViewById<TextView>(R.id.textDate)
            val docView = row.findViewById<TextView>(R.id.textDoctor)
            val patientView = row.findViewById<TextView>(R.id.textPatient)
            val statusView = row.findViewById<TextView>(R.id.textStatus)

            try {
                if (text.contains("|")) {
                    dateView.text = text.substringBefore("|").trim()
                    val rest = text.substringAfter("|").trim()
                    docView.text = rest.substringBefore("-").trim()
                    patientView.text = rest.substringAfter("-").trim()
                } else {
                    dateView.text = text
                    docView.text = ""
                    patientView.text = ""
                }
            } catch (_: Exception) {
                dateView.text = text
            }

            if (text.contains("[Confirmed]", ignoreCase = true)) {
                statusView.text = "CONFIRMED"
            } else if (text.contains("[Pending]", ignoreCase = true)) {
                statusView.text = "PENDING"
            } else if (text.contains("[Cancelled]", ignoreCase = true)) {
                statusView.text = "CANCELLED"
            } else {
                statusView.text = "SCHEDULED"
            }

            row.setOnClickListener {
                showAppointmentActionDialog(text)
            }
        }
        recycler.adapter = adapter

        val key = cleanName.lowercase()

        viewModel.formattedAppointmentList.observe(viewLifecycleOwner) { all ->
            val mine = all.filter { it.lowercase().contains("dr. $key") || it.lowercase().contains(key) }
            mineCache = mine

            adapter.submit(mine)
            empty.visibility = if (mine.isEmpty()) View.VISIBLE else View.GONE

            val confirmed = mine.count { it.contains("[Confirmed]", ignoreCase = true) }
            val pending = mine.count { it.contains("[Pending]", ignoreCase = true) }
            summary.text = "Total Appointments: ${mine.size}  ·  Confirmed: $confirmed  ·  Pending: $pending"
        }

        view.findViewById<Button>(R.id.btnWriteConsultation).setOnClickListener {
            if (mineCache.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "No appointments to document yet",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Choose an appointment")
                    .setItems(mineCache.toTypedArray()) { _, index ->
                        openConsultation(mineCache[index])
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            authViewModel.logout()
        }
    }

    private fun showAppointmentActionDialog(appointmentText: String) {
        val options = arrayOf("Confirm / Approve Appointment", "Cancel Appointment", "Write Consultation Notes")
        AlertDialog.Builder(requireContext())
            .setTitle("Manage Patient Appointment")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        viewModel.appointments.value?.firstOrNull { appt ->
                            appointmentText.contains(appt.date) && appointmentText.contains(appt.time) && appointmentText.contains(appt.patientName)
                        }?.let { appt ->
                            viewModel.updateAppointmentStatus(appt.id, "Confirmed")
                            Toast.makeText(requireContext(), "Appointment confirmed & approved!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    1 -> {
                        viewModel.appointments.value?.firstOrNull { appt ->
                            appointmentText.contains(appt.date) && appointmentText.contains(appt.time) && appointmentText.contains(appt.patientName)
                        }?.let { appt ->
                            viewModel.cancelAppointment(appt.id)
                            Toast.makeText(requireContext(), "Appointment cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                    2 -> {
                        openConsultation(appointmentText)
                    }
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun openConsultation(appointmentText: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ConsultationFragment.newInstance(appointmentText))
            .addToBackStack(null)
            .commit()
    }
}
