package com.example.healthappointmentmanagementsystem.ui.patient

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.data.IntakeDirectory
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IntakeFormFragment : Fragment(R.layout.fragment_intake_form) {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val symptoms = view.findViewById<EditText>(R.id.editSymptoms)
        val allergies = view.findViewById<EditText>(R.id.editAllergies)
        val meds = view.findViewById<EditText>(R.id.editMeds)

        view.findViewById<Button>(R.id.btnSubmitIntake).setOnClickListener {
            val symText = symptoms.text.toString().trim()
            val allText = allergies.text.toString().trim()
            val medText = meds.text.toString().trim()

            if (symText.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your symptoms.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val patient = authViewModel.currentUser.value
            val patientName = patient?.name ?: "Anonymous Patient"
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            viewLifecycleOwner.lifecycleScope.launch {
                IntakeDirectory.save(
                    requireContext(),
                    IntakeDirectory.IntakeRecord(
                        patientName = patientName,
                        symptoms = symText,
                        allergies = if (allText.isEmpty()) "None recorded" else allText,
                        medications = if (medText.isEmpty()) "None recorded" else medText,
                        date = currentDate
                    )
                )
                Toast.makeText(requireContext(), "Intake form submitted successfully! Doctor can now review it.", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            }
        }
    }
}
