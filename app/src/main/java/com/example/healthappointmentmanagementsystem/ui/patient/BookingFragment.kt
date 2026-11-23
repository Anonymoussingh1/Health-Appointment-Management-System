package com.example.healthappointmentmanagementsystem.ui.patient

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.data.Appointment
import com.example.healthappointmentmanagementsystem.data.UserDirectory
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class BookingFragment : Fragment(R.layout.fragment_booking) {

    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private val timeSlots = listOf(
        "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
        "02:00 PM", "03:00 PM", "04:00 PM"
    )

    private var doctorNames: List<String> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val doctorInput = view.findViewById<AutoCompleteTextView>(R.id.inputDoctor)
        val doctorHint = view.findViewById<TextView>(R.id.textDoctorHint)
        val dateInput = view.findViewById<EditText>(R.id.inputDate)
        val timeInput = view.findViewById<AutoCompleteTextView>(R.id.inputTime)
        val reasonInput = view.findViewById<EditText>(R.id.inputReason)
        val error = view.findViewById<TextView>(R.id.textError)
        val confirm = view.findViewById<Button>(R.id.btnConfirm)

        timeInput.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, timeSlots)
        )

        // Load the registered doctors from the database.
        viewLifecycleOwner.lifecycleScope.launch {
            val doctors = UserDirectory.byRole(requireContext(), "DOCTOR")
            doctorNames = doctors.map { "Dr. ${it.name}" }
            doctorInput.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, doctorNames)
            )
            doctorInput.threshold = 0
            doctorHint.text =
                if (doctorNames.isEmpty()) "No doctors are registered yet"
                else "Type to search or tap to choose a registered doctor"
        }

        doctorInput.setOnClickListener { doctorInput.showDropDown() }
        doctorInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) doctorInput.showDropDown()
        }

        dateInput.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    dateInput.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).apply { datePicker.minDate = System.currentTimeMillis() }.show()
        }

        timeInput.setOnClickListener { timeInput.showDropDown() }

        confirm.setOnClickListener {
            val typedDoctor = doctorInput.text.toString().trim()
            val date = dateInput.text.toString()
            val time = timeInput.text.toString()
            val reason = reasonInput.text.toString().trim()

            if (typedDoctor.isEmpty() || date.isEmpty() || time.isEmpty() || reason.isEmpty()) {
                showError(error, "Please fill in every field")
                return@setOnClickListener
            }

            val chosenDoctor = doctorNames.firstOrNull { it.equals(typedDoctor, ignoreCase = true) }
            if (chosenDoctor == null) {
                showError(error, "Choose a doctor from the registered list")
                return@setOnClickListener
            }

            val patient = authViewModel.currentUser.value
            if (patient == null) {
                showError(error, "Please log in again")
                return@setOnClickListener
            }

            val appointment = Appointment(
                id = UUID.randomUUID().toString(),
                patientName = patient.name,
                doctorName = chosenDoctor.removePrefix("Dr. ").trim(),
                date = date,
                time = time,
                status = "Pending"
            )

            confirm.isEnabled = false
            error.visibility = View.GONE
            appointmentViewModel.addAppointment(appointment) { problem ->
                confirm.isEnabled = true
                if (problem == null) {
                    Toast.makeText(requireContext(), "Appointment booked", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    showError(error, problem)
                }
            }
        }

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showError(error: TextView, message: String) {
        error.text = message
        error.visibility = View.VISIBLE
    }
}