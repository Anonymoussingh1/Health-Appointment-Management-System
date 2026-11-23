package com.example.healthappointmentmanagementsystem.ui.doctor

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthappointmentmanagementsystem.R

class DoctorScheduleFragment : Fragment(R.layout.fragment_doctor_schedule) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val startTime = view.findViewById<EditText>(R.id.editStartTime)
        val endTime = view.findViewById<EditText>(R.id.editEndTime)
        val blockDate = view.findViewById<EditText>(R.id.editBlockDate)

        view.findViewById<Button>(R.id.btnSaveSchedule).setOnClickListener {
            val start = startTime.text.toString().trim()
            val end = endTime.text.toString().trim()
            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both start and end shift times.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(requireContext(), "Shift updated successfully: $start to $end", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnBlockTime).setOnClickListener {
            val date = blockDate.text.toString().trim()
            if (date.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter the date to block.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(requireContext(), "Time off blocked successfully for $date", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
        }
    }
}
