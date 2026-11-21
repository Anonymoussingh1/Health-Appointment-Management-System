package com.example.healthappointmentmanagementsystem.ui.doctors

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R

class DoctorListFragment : Fragment(R.layout.fragment_doctor_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.recyclerDoctors).layoutManager =
            LinearLayoutManager(requireContext())
    }
}