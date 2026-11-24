package com.example.healthappointmentmanagementsystem.ui.admin

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.data.UserDirectory.Person
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter
import com.google.android.material.textfield.TextInputEditText

class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {

    private val appointmentViewModel: AppointmentViewModel by activityViewModels()
    private val adminViewModel: AdminViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private var allDoctors: List<Person> = emptyList()
    private var allPatients: List<Person> = emptyList()
    private var appointmentCount = 0
    private var query = ""

    private lateinit var doctorAdapter: SimpleListAdapter<Person>
    private lateinit var patientAdapter: SimpleListAdapter<Person>
    private lateinit var summary: TextView
    private lateinit var noDoctors: TextView
    private lateinit var noPatients: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = authViewModel.currentUser.value
        summary = view.findViewById(R.id.textSummary)
        noDoctors = view.findViewById(R.id.textNoDoctors)
        noPatients = view.findViewById(R.id.textNoPatients)

        view.findViewById<TextView>(R.id.textWelcome).text = "Admin dashboard"
        view.findViewById<TextView>(R.id.textProfile).text =
            "Name: ${user?.name ?: "Admin"}\nEmail: ${user?.email ?: "-"}\nRole: Admin"

        doctorAdapter = buildUserList(view, R.id.recyclerDoctors, "DOCTOR")
        patientAdapter = buildUserList(view, R.id.recyclerPatients, "PATIENT")

        view.findViewById<Button>(R.id.btnAddDoctor).setOnClickListener {
            showAddDialog("DOCTOR")
        }
        view.findViewById<Button>(R.id.btnAddPatient).setOnClickListener {
            showAddDialog("PATIENT")
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            authViewModel.logout()
        }

        view.findViewById<TextInputEditText>(R.id.inputSearch).doAfterTextChanged {
            query = it?.toString().orEmpty().trim().lowercase()
            render()
        }

        adminViewModel.doctors.observe(viewLifecycleOwner) {
            allDoctors = it
            render()
        }
        adminViewModel.patients.observe(viewLifecycleOwner) {
            allPatients = it
            render()
        }

        val apptRecycler = view.findViewById<RecyclerView>(R.id.recyclerAppointments)
        val noAppointments = view.findViewById<TextView>(R.id.textNoAppointments)
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
                AlertDialog.Builder(requireContext())
                    .setTitle("Manage Appointment")
                    .setMessage("What would you like to do with this appointment?")
                    .setPositiveButton("Delete Appointment") { _, _ ->
                        appointmentViewModel.appointments.value?.firstOrNull { appt ->
                            text.contains(appt.date) && text.contains(appt.time) && text.contains(appt.patientName)
                        }?.let { appt ->
                            appointmentViewModel.deleteAppointment(appt.id)
                            Toast.makeText(requireContext(), "Appointment deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNeutralButton("Cancel Appointment") { _, _ ->
                        appointmentViewModel.appointments.value?.firstOrNull { appt ->
                            text.contains(appt.date) && text.contains(appt.time) && text.contains(appt.patientName)
                        }?.let { appt ->
                            appointmentViewModel.cancelAppointment(appt.id)
                            Toast.makeText(requireContext(), "Appointment status changed to Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Close", null)
                    .show()
            }
        }
        apptRecycler.adapter = apptAdapter

        appointmentViewModel.formattedAppointmentList.observe(viewLifecycleOwner) { list ->
            apptAdapter.submit(list)
            appointmentCount = list.size
            noAppointments.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            renderSummary()
        }
    }

    private fun renderSummary() {
        summary.text = "Doctors: ${allDoctors.size}  ·  Patients: ${allPatients.size}  ·  Appointments: $appointmentCount"
    }

    private fun render() {
        val filteredDocs = allDoctors.filter {
            it.name.lowercase().contains(query) || it.email.lowercase().contains(query)
        }
        val filteredPatients = allPatients.filter {
            it.name.lowercase().contains(query) || it.email.lowercase().contains(query)
        }

        doctorAdapter.submit(filteredDocs)
        patientAdapter.submit(filteredPatients)

        noDoctors.visibility = if (filteredDocs.isEmpty()) View.VISIBLE else View.GONE
        noPatients.visibility = if (filteredPatients.isEmpty()) View.VISIBLE else View.GONE

        renderSummary()
    }

    private fun showAddDialog(role: String) {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        val nameInput = TextInputEditText(requireContext()).apply { hint = "Full Name" }
        val emailInput = TextInputEditText(requireContext()).apply { hint = "Email Address"; inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS }
        val passInput = TextInputEditText(requireContext()).apply { hint = "Password"; inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD }
        layout.addView(nameInput)
        layout.addView(emailInput)
        layout.addView(passInput)

        AlertDialog.Builder(requireContext())
            .setTitle("Add new $role")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val pass = passInput.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                    adminViewModel.addUser(name, email, pass, role) { error ->
                        if (error == null) {
                            Toast.makeText(requireContext(), "$role added successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun buildUserList(view: View, recyclerId: Int, role: String): SimpleListAdapter<Person> {
        val recycler = view.findViewById<RecyclerView>(recyclerId)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SimpleListAdapter<Person>(R.layout.item_appointment) { row, person ->
            row.findViewById<TextView>(R.id.textDate).text = person.name
            row.findViewById<TextView>(R.id.textDoctor).text = person.email
            row.findViewById<TextView>(R.id.textStatus).text = role
            row.findViewById<TextView>(R.id.textStatus).setBackgroundResource(R.drawable.bg_badge)

            row.setOnClickListener {
                confirmRemove(person, role, if (role == "DOCTOR") "doctor" else "patient")
            }
        }
        recycler.adapter = adapter
        return adapter
    }

    private fun confirmRemove(person: Person, role: String, label: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove $label?")
            .setMessage("Remove ${person.name} (${person.email})?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Remove") { _, _ ->
                adminViewModel.removeUser(person, role)
                Toast.makeText(requireContext(), "Removed successfully", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
