package com.example.healthappointmentmanagementsystem.ui.doctor

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

class WaitingRoomFragment : Fragment(R.layout.fragment_waiting_room) {
    private val viewModel: AppointmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerWaiting)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = SimpleListAdapter<String>(R.layout.item_simple_row) { row, text ->
            row.findViewById<TextView>(R.id.textTitle).text = text
        }
        recycler.adapter = adapter

        viewModel.formattedAppointmentList.observe(viewLifecycleOwner) { all ->
            val checkedIn = all.filter { it.contains("Confirmed") || it.contains("Pending") }
            adapter.submit(checkedIn)
        }
    }
}
