package com.example.healthappointmentmanagementsystem.ui.patient

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.data.PrescriptionDirectory
import com.example.healthappointmentmanagementsystem.ui.auth.AuthViewModel
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrescriptionsFragment : Fragment(R.layout.fragment_prescriptions) {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerPrescriptions)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val meds = listOf(
            "Amoxicillin 500mg — Take 3x daily with food (Active)",
            "Paracetamol 500mg — Take as needed for fever/pain (Active)"
        )

        val adapter = SimpleListAdapter<String>(R.layout.item_simple_row) { row, text ->
            row.findViewById<TextView>(R.id.textTitle).text = text
        }
        recycler.adapter = adapter
        adapter.submit(meds)

        view.findViewById<Button>(R.id.btnRequestRefill).setOnClickListener {
            val patient = authViewModel.currentUser.value
            val patientName = patient?.name ?: "Aman"
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            PrescriptionDirectory.addRequest(
                requireContext(),
                PrescriptionDirectory.RefillRequest(
                    patientName = patientName,
                    medication = "Amoxicillin 500mg (Refill)",
                    date = currentDate
                )
            )

            Toast.makeText(requireContext(), "Prescription refill request successfully sent to Dr. Lee.", Toast.LENGTH_LONG).show()
        }
    }
}
