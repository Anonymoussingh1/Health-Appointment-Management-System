package com.example.healthappointmentmanagementsystem.ui.admin

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
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
import com.example.healthappointmentmanagementsystem.data.Appointment
import com.example.healthappointmentmanagementsystem.data.UserDirectory.Person
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter
import com.google.android.material.textfield.TextInputEditText

class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {

    private val appointmentViewModel: AppointmentViewModel by viewModels()
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
        val apptAdapter = SimpleListAdapter<Appointment>(R.layout.item_admin_appointment) { row, a ->
            row.findViewById<TextView>(R.id.textTitle).text =
                "${a.date} at ${a.time}\nDr. ${a.doctorName}  ·  Patient: ${a.patientName}\nStatus: ${a.status}"

            val cancel = row.findViewById<View>(R.id.btnCancel)
            cancel.visibility =
                if (a.status.equals("Cancelled", ignoreCase = true)) View.GONE else View.VISIBLE
            cancel.setOnClickListener { confirmCancel(a) }
            row.findViewById<View>(R.id.btnDelete).setOnClickListener { confirmDelete(a) }
        }
        apptRecycler.adapter = apptAdapter

        appointmentViewModel.appointments.observe(viewLifecycleOwner) { all ->
            apptAdapter.submit(all)
            noAppointments.visibility = if (all.isEmpty()) View.VISIBLE else View.GONE
            appointmentCount = all.size
            render()
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            authViewModel.logout()
        }
    }

    private fun matches(person: Person) =
        query.isEmpty() ||
                person.name.lowercase().contains(query) ||
                person.email.lowercase().contains(query)

    private fun render() {
        val doctors = allDoctors.filter { matches(it) }
        val patients = allPatients.filter { matches(it) }

        doctorAdapter.submit(doctors)
        patientAdapter.submit(patients)
        noDoctors.visibility = if (doctors.isEmpty()) View.VISIBLE else View.GONE
        noPatients.visibility = if (patients.isEmpty()) View.VISIBLE else View.GONE

        summary.text = "Doctors: ${allDoctors.size}  ·  Patients: ${allPatients.size}  ·  " +
                "Appointments: $appointmentCount"
    }

    private fun buildUserList(view: View, recyclerId: Int, role: String): SimpleListAdapter<Person> {
        val recycler = view.findViewById<RecyclerView>(recyclerId)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SimpleListAdapter<Person>(R.layout.item_user_row) { row, person ->
            val label = if (role == "DOCTOR") "Dr. ${person.name}" else person.name
            row.findViewById<TextView>(R.id.textName).text = label
            row.findViewById<TextView>(R.id.textEmail).text = person.email
            row.findViewById<View>(R.id.btnRemove).setOnClickListener {
                confirmRemove(person, role, label)
            }
        }
        recycler.adapter = adapter
        return adapter
    }

    private fun confirmCancel(appointment: Appointment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Cancel this appointment?")
            .setMessage("${appointment.patientName} with Dr. ${appointment.doctorName} on ${appointment.date} at ${appointment.time}.")
            .setNegativeButton("Keep", null)
            .setPositiveButton("Cancel appointment") { _, _ ->
                appointmentViewModel.cancelAppointment(appointment.id)
                Toast.makeText(requireContext(), "Appointment cancelled", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun confirmDelete(appointment: Appointment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete this appointment?")
            .setMessage("It will be removed permanently from the system.")
            .setNegativeButton("Keep", null)
            .setPositiveButton("Delete") { _, _ ->
                appointmentViewModel.deleteAppointment(appointment.id)
                Toast.makeText(requireContext(), "Appointment deleted", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun confirmRemove(person: Person, role: String, label: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove $label?")
            .setMessage(
                "They will no longer be able to log in. " +
                        "Appointments that mention them are kept."
            )
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Remove") { _, _ ->
                adminViewModel.removeUser(person, role)
                Toast.makeText(requireContext(), "$label removed", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showAddDialog(role: String) {
        val content = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_user, null)
        val nameInput = content.findViewById<TextInputEditText>(R.id.inputName)
        val emailInput = content.findViewById<TextInputEditText>(R.id.inputEmail)
        val passwordInput = content.findViewById<TextInputEditText>(R.id.inputPassword)
        val error = content.findViewById<TextView>(R.id.textDialogError)

        val title = if (role == "DOCTOR") "Add doctor" else "Add patient"
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(content)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Add", null)
            .create()
        dialog.show()

        // Set the click listener after show() so a validation error keeps the dialog open.
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            val problem = when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() ->
                    "Please fill in every field"
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    "Enter a valid email address"
                password.length < 6 ->
                    "The password needs at least 6 characters"
                else -> null
            }
            if (problem != null) {
                error.text = problem
                error.visibility = View.VISIBLE
                return@setOnClickListener
            }

            adminViewModel.addUser(name, email, password, role) { result ->
                if (result == null) {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "$title: done", Toast.LENGTH_SHORT).show()
                } else {
                    error.text = result
                    error.visibility = View.VISIBLE
                }
            }
        }
    }
}