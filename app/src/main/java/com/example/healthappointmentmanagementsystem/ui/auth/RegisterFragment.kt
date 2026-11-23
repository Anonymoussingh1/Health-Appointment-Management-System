package com.example.healthappointmentmanagementsystem.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.healthappointmentmanagementsystem.R

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<EditText>(R.id.editName)
        val email = view.findViewById<EditText>(R.id.editEmail)
        val password = view.findViewById<EditText>(R.id.editPassword)
        val roleGroup = view.findViewById<RadioGroup>(R.id.radioRole)
        val error = view.findViewById<TextView>(R.id.textError)

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val role = if (roleGroup.checkedRadioButtonId == R.id.radioDoctor) "DOCTOR" else "PATIENT"
            authViewModel.register(
                name.text.toString().trim(),
                email.text.toString().trim(),
                password.text.toString(),
                role
            )
        }

        authViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            error.text = message
            error.visibility = if (message.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }
}