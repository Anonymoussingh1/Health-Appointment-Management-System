package com.example.healthappointmentmanagementsystem.ui.patient

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthappointmentmanagementsystem.R

class BillingFragment : Fragment(R.layout.fragment_billing) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnPayNow).setOnClickListener {
            Toast.makeText(requireContext(), "Payment of $25.00 processed successfully via Insurance/Card!", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
        }
    }
}
