package com.example.healthappointmentmanagementsystem.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.healthappointmentmanagementsystem.R
import com.google.android.material.button.MaterialButtonToggleGroup

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = view.findViewById<EditText>(R.id.editEmail)
        val password = view.findViewById<EditText>(R.id.editPassword)
        val error = view.findViewById<TextView>(R.id.textError)
        val toggle = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleRole)

        toggle.check(R.id.btnRolePatient)

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val selectedRole = when (toggle.checkedButtonId) {
                R.id.btnRoleDoctor -> "Doctor"
                R.id.btnRoleAdmin -> "Admin"
                else -> "Patient"
            }
            authViewModel.login(
                email.text.toString().trim(),
                password.text.toString(),
                selectedRole
            )
        }

        view.findViewById<TextView>(R.id.textGoRegister).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        authViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            error.text = message
            error.visibility = if (message.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        authViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                // Routing by role is handled in MainActivity.
                Toast.makeText(requireContext(), "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}