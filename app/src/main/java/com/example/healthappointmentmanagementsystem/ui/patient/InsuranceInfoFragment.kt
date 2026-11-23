package com.example.healthappointmentmanagementsystem.ui.patient

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthappointmentmanagementsystem.R

class InsuranceInfoFragment : Fragment(R.layout.fragment_insurance_info) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnUpdateInsurance).setOnClickListener {
            Toast.makeText(requireContext(), "Insurance details updated successfully.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
