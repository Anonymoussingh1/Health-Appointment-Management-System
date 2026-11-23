package com.example.healthappointmentmanagementsystem.ui.appointments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthappointmentmanagementsystem.R
import com.example.healthappointmentmanagementsystem.ui.AppointmentViewModel
import com.example.healthappointmentmanagementsystem.ui.common.SimpleListAdapter

class AppointmentListFragment : Fragment(R.layout.fragment_appointment_list) {

    private val viewModel: AppointmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerAppointments)
        val empty = view.findViewById<TextView>(R.id.textEmpty)

        val adapter = SimpleListAdapter<String>(R.layout.item_appointment) { row, text ->
            row.findViewById<TextView>(R.id.textTitle).text = text
        }

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewModel.formattedAppointmentList.observe(viewLifecycleOwner) { list ->
            adapter.submit(list)
            empty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}