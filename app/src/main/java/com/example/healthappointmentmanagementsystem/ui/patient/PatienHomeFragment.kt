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
        val name = user?.name ?: "there"

        view.findViewById<TextView>(R.id.textWelcome).text = "Hello, $name"
        view.findViewById<TextView>(R.id.textProfile).text =
            "Name: $name\nEmail: ${user?.email ?: "-"}\nRole: Patient"

        view.findViewById<Button>(R.id.btnBook).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingFragment())
                .addToBackStack(null)
                .commit()
        }

        // Empty states until prescriptions and consultations exist.
        val meds = emptyList<String>()
        val seenDoctors = emptyList<String>()

        setupList(view.findViewById(R.id.recyclerMeds), meds, view.findViewById(R.id.textNoMeds))
        setupList(view.findViewById(R.id.recyclerDoctorsSeen), seenDoctors, view.findViewById(R.id.textNoDoctors))

        // Show only this patient's appointments.
        // TEMPORARY: matches on the patient's name until Appointment has a patientId.
        val apptRecycler = view.findViewById<RecyclerView>(R.id.recyclerAppointments)
        val apptEmpty = view.findViewById<TextView>(R.id.textEmpty)
        val summary = view.findViewById<TextView>(R.id.textSummary)

        apptRecycler.layoutManager = LinearLayoutManager(requireContext())
        val apptAdapter = SimpleListAdapter<String>(R.layout.item_appointment) { row, text ->
            row.findViewById<TextView>(R.id.textTitle).text = text
        }
        apptRecycler.adapter = apptAdapter

        viewModel.formattedAppointmentList.observe(viewLifecycleOwner) { all ->
            val key = "patient: ${name.trim().lowercase()}"
            val mine = all.filter { it.lowercase().contains(key) }

            apptAdapter.submit(mine)
            apptEmpty.visibility = if (mine.isEmpty()) View.VISIBLE else View.GONE

            val confirmed = mine.count { it.contains("[Confirmed]") }
            val pending = mine.count { it.contains("[Pending]") }
            val cancelled = mine.count { it.contains("[Cancelled]") }
            summary.text = "Upcoming: ${mine.size}  ·  Confirmed: $confirmed  ·  " +
                    "Pending: $pending  ·  Cancelled: $cancelled"
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            authViewModel.logout()
        }
    }

    private fun setupList(recycler: RecyclerView, items: List<String>, emptyView: TextView) {
        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SimpleListAdapter<String>(R.layout.item_simple_row) { row, text ->
            row.findViewById<TextView>(R.id.textTitle).text = text
        }
        recycler.adapter = adapter
        adapter.submit(items)
        emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }
}