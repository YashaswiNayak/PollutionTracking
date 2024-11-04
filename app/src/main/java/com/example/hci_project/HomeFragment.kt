package com.example.hci_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val citySpinner: Spinner = view.findViewById(R.id.citySpinner)
        val fetchDataButton: Button = view.findViewById(R.id.fetchDataButton)

        // City names list with "Others" option
        val cityList = listOf("Delhi", "Mumbai", "Hyderabad", "Bengaluru", "Others")

        // Adapter for the Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        fetchDataButton.setOnClickListener {
            val selectedCity = citySpinner.selectedItem.toString()

            if (selectedCity == "Others") {
                showCoordinateInputDialog()
            } else {
                fetchPollutionDataForCity(selectedCity)
            }
        }
    }

    private fun showCoordinateInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_coordinates, null)
        val latitudeInput: EditText = dialogView.findViewById(R.id.latitudeInput)
        val longitudeInput: EditText = dialogView.findViewById(R.id.longitudeInput)

        AlertDialog.Builder(requireContext())
            .setTitle("Enter Coordinates")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val latitude = latitudeInput.text.toString()
                val longitude = longitudeInput.text.toString()

                if (latitude.isNotBlank() && longitude.isNotBlank()) {
                    fetchPollutionDataForCoordinates(latitude, longitude)
                } else {
                    Toast.makeText(requireContext(), "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun fetchPollutionDataForCity(city: String) {
        Toast.makeText(requireContext(), "Fetching data for $city", Toast.LENGTH_SHORT).show()
    }

    private fun fetchPollutionDataForCoordinates(latitude: String, longitude: String) {
        Toast.makeText(requireContext(), "Fetching data for coordinates: $latitude, $longitude", Toast.LENGTH_SHORT).show()
    }
}
