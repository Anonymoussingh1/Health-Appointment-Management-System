package com.example.healthappointmentmanagementsystem.ui.patient

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthappointmentmanagementsystem.R

class MessagesFragment : Fragment(R.layout.fragment_messages) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editMessage = view.findViewById<EditText>(R.id.editMessage)

        view.findViewById<Button>(R.id.btnSend).setOnClickListener {
            val msg = editMessage.text.toString().trim()
            if (msg.isEmpty()) {
                Toast.makeText(requireContext(), "Please type a message first.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(requireContext(), "Message sent to care team!", Toast.LENGTH_SHORT).show()
            editMessage.setText("")
        }
    }
}
