package com.example.healthappointmentmanagementsystem.ui.patient

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.healthappointmentmanagementsystem.R

class FamilyProfilesFragment : Fragment(R.layout.fragment_family_profiles) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textProfilesList = view.findViewById<TextView>(R.id.textProfilesList)

        val dependents = mutableListOf("• Sarah Bhamra (Spouse)", "• Junior Bhamra (Child)")

        fun updateList() {
            textProfilesList.text = dependents.joinToString("\n")
        }

        view.findViewById<Button>(R.id.btnAddDependent).setOnClickListener {
            val layout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 40, 50, 10)
            }
            val nameInput = EditText(requireContext()).apply {
                hint = "Dependent Name (e.g. Jane Doe)"
                setPadding(20, 20, 20, 20)
            }
            val relationInput = EditText(requireContext()).apply {
                hint = "Relation (e.g. Child, Spouse)"
                setPadding(20, 20, 20, 20)
            }
            layout.addView(nameInput)
            layout.addView(relationInput)

            AlertDialog.Builder(requireContext())
                .setTitle("Add Family Dependent")
                .setMessage("Enter the details of the family member:")
                .setView(layout)
                .setPositiveButton("Add") { _, _ ->
                    val name = nameInput.text.toString().trim()
                    val relation = relationInput.text.toString().trim()
                    if (name.isNotEmpty() && relation.isNotEmpty()) {
                        dependents.add("• $name ($relation)")
                        updateList()
                        Toast.makeText(requireContext(), "Added $name ($relation) successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Please enter both name and relation", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
