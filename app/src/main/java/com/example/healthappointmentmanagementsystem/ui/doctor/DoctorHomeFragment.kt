package com.example.healthappointmentmanagementsystem.ui.doctor

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter
import com.example.healthappointmentmanagementsystem.ui.consultation.ConsultationFragment

class DoctorHomeFragment : Fragment(R.layout.fragment_doctor_home) {

    private val viewModel: AppointmentViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private var mineCache: List<String> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = authViewModel.currentUser.value
        val name = user?.name ?: "Doctor"

        view.findViewById<TextView>(R.id.textWelcome).text = "Dr. $name"
        view.findViewById<TextView>(R.id.textProfile).text =
            "Name: Dr. $name\nEmail: ${user?.email ?: "-"}\nRole: Doctor${if (!user?.specialty.isNullOrEmpty()) "\nSpecialty: ${user?.specialty}" else ""}${if (!user?.hospital.isNullOrEmpty()) "\nHospital: ${user?.hospital}" else ""}"

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
            row.findViewById<TextView>(R.id.textTitle).text = text
            row.setOnClickListener { openConsultation(text) }
        }
        recycler.adapter = adapter

        val key = "dr. ${name.trim().lowercase()}"

        viewModel.formattedAppointmentList.observe(viewLifecycleOwner) { all ->
            val mine = all.filter { it.lowercase().contains(key) }
            mineCache = mine

            adapter.submit(mine)
            empty.visibility = if (mine.isEmpty()) View.VISIBLE else View.GONE

            val confirmed = mine.count { it.contains("[Confirmed]") }
            val pending = mine.count { it.contains("[Pending]") }
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

    private fun openConsultation(appointmentText: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ConsultationFragment.newInstance(appointmentText))
            .addToBackStack(null)
            .commit()
    }
}
