package com.example.healthappointmentmanagementsystem.ui.auth

import android.app.AlertDialog
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

        toggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                error.visibility = View.GONE
                if (checkedId == R.id.btnRoleAdmin) {
                    email.setText(AuthViewModel.DEFAULT_ADMIN_EMAIL)
                    password.setText(AuthViewModel.DEFAULT_ADMIN_PASSWORD)
                } else {
                    if (email.text.toString() == AuthViewModel.DEFAULT_ADMIN_EMAIL) {
                        email.setText("")
                        password.setText("")
                    }
                }
            }
        }

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

        view.findViewById<TextView>(R.id.textForgotPassword).setOnClickListener {
            val input = EditText(requireContext())
            input.hint = "Enter your account email"
            input.setPadding(40, 30, 40, 30)

            AlertDialog.Builder(requireContext())
                .setTitle("Reset Password")
                .setMessage("Enter your email address and we will send you a password reset link.")
                .setView(input)
                .setPositiveButton("Send Link") { _, _ ->
                    val mail = input.text.toString().trim()
                    if (mail.isNotEmpty()) {
                        Toast.makeText(requireContext(), "Password reset link sent to $mail", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
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
                Toast.makeText(requireContext(), "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
