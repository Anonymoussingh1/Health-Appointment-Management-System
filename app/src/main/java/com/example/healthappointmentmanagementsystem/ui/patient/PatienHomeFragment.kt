package com.example.healthappointmentmanagementsystem.ui.patient

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter

class PatientHomeFragment : Fragment(R.layout.fragment_patient_home) {

    private val viewModel: AppointmentViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = authViewModel.currentUser.value
        val name = user?.name ?: "Patient"

        view.findViewById<TextView>(R.id.textWelcome).text = "Hello, $name"
        view.findViewById<TextView>(R.id.textProfile).text =
            "Logged in as: $name\nEmail: ${user?.email ?: "-"}\nRole: Patient"

        view.findViewById<Button>(R.id.btnBook).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingFragment())
                .addToBackStack(null)
                .commit()
        }

        // Patient Quick Action Services (Opening dedicated pages/forms)
        view.findViewById<Button>(R.id.btnIntake).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, IntakeFormFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnPrescriptions).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PrescriptionsFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnBilling).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BillingFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnMessages).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MessagesFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnDependents).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FamilyProfilesFragment())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.btnInsurance).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, InsuranceInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        // Show only this patient's appointments with structured card bindings.
        val apptRecycler = view.findViewById<RecyclerView>(R.id.recyclerAppointments)
        val apptEmpty = view.findViewById<TextView>(R.id.textEmpty)
        val summary = view.findViewById<TextView>(R.id.textSummary)

        apptRecycler.layoutManager = LinearLayoutManager(requireContext())
        val apptAdapter = SimpleListAdapter<String>(R.layout.item_appointment) { row, text ->
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
            } catch (e: Exception) {
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
        }
        apptRecycler.adapter = apptAdapter

        viewModel.formattedAppointmentList.observe(viewLifecycleOwner) { all ->
            val key = "patient: ${name.trim().lowercase()}"
            val mine = all.filter { it.lowercase().contains(key) }

            apptAdapter.submit(mine)
            apptEmpty.visibility = if (mine.isEmpty()) View.VISIBLE else View.GONE

            val confirmed = mine.count { it.contains("[Confirmed]", ignoreCase = true) }
            val pending = mine.count { it.contains("[Pending]", ignoreCase = true) }
            val cancelled = mine.count { it.contains("[Cancelled]", ignoreCase = true) }
            summary.text = "Appointments — Total: ${mine.size}  ·  Confirmed: $confirmed  ·  Pending: $pending  ·  Cancelled: $cancelled"
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            authViewModel.logout()
        }
    }
}
