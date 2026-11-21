package com.example.healthappointmentmanagementsystem.ui.appointments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R

class AppointmentListFragment : Fragment(R.layout.fragment_appointment_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.recyclerAppointments).layoutManager =
            LinearLayoutManager(requireContext())
    }
}