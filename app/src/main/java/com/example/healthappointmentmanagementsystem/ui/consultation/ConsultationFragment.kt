package com.example.healthappointmentmanagementsystem.ui.consultation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.healthappointmentmanagementsystem.R
import com.google.android.material.textfield.TextInputEditText

class ConsultationFragment : Fragment(R.layout.fragment_consultation) {

    companion object {
        private const val ARG_APPOINTMENT = "appointment"

        fun newInstance(appointmentText: String): ConsultationFragment {
            return ConsultationFragment().apply {
                arguments = Bundle().apply { putString(ARG_APPOINTMENT, appointmentText) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textAppointment).text =
            arguments?.getString(ARG_APPOINTMENT).orEmpty()

        val diagnosis = view.findViewById<TextInputEditText>(R.id.inputDiagnosis)
        val advice = view.findViewById<TextInputEditText>(R.id.inputAdvice)
        val medications = view.findViewById<TextInputEditText>(R.id.inputMedications)
        val error = view.findViewById<TextView>(R.id.textError)

        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            if (diagnosis.text.isNullOrBlank()) {
                error.text = "Enter a diagnosis before saving"
                error.visibility = View.VISIBLE
                return@setOnClickListener
            }
            // Saving needs the Consultation table (Stage 3). Until it exists,
            // the notes are not stored, so say so instead of pretending to save.
            error.text = "Saving is not connected yet: consultations need the database table"
            error.visibility = View.VISIBLE
        }

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}