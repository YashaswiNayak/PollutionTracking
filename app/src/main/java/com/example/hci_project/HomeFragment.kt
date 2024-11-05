package com.example.hci_project

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var pollutionRecyclerView: RecyclerView
    private lateinit var pollutantAdapter: PollutantAdapter

    // List to store favorite coordinates
    private val favoriteCoordinates = mutableListOf<Pair<String, String>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val citySpinner: Spinner = view.findViewById(R.id.citySpinner)
        val fetchDataButton: Button = view.findViewById(R.id.fetchDataButton)
        val clearButton: Button = view.findViewById(R.id.clearButton)
        pollutionRecyclerView = view.findViewById(R.id.pollutionRecyclerView)

        // Initialize RecyclerView
        pollutionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Prepare city list and include favorite coordinates
        val cityList = prepareCityList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        fetchDataButton.setOnClickListener {
            val selectedCity = citySpinner.selectedItem.toString()
            if (selectedCity == "Others") {
                showCoordinateInputDialog()
            } else {
                displayPollutionDataForCity(selectedCity)
            }
        }

        // Clear button functionality
        clearButton.setOnClickListener {
            clearPollutionData()
        }
    }

    private fun prepareCityList(): List<String> {
        // Create a list of cities and add favorites
        val cityList = mutableListOf("Delhi", "Mumbai", "Hyderabad", "Bengaluru", "Others")
        favoriteCoordinates.forEach { coordinates ->
            cityList.add("${coordinates.first}, ${coordinates.second}")
        }
        return cityList
    }

    private fun clearPollutionData() {
        // Clear the adapter data by setting an empty list
        pollutantAdapter = PollutantAdapter(emptyList())
        pollutionRecyclerView.adapter = pollutantAdapter
        Toast.makeText(requireContext(), "Data cleared", Toast.LENGTH_SHORT).show()
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
                    addFavoriteCoordinate(latitude, longitude)
                    displayPollutionDataForCoordinates(latitude, longitude)
                } else {
                    Toast.makeText(requireContext(), "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun addFavoriteCoordinate(latitude: String, longitude: String) {
        favoriteCoordinates.add(Pair(latitude, longitude))
        // Update the city spinner to include the new favorite coordinates
        val citySpinner: Spinner = requireView().findViewById(R.id.citySpinner)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prepareCityList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter
        Toast.makeText(requireContext(), "Coordinate added to favorites", Toast.LENGTH_SHORT).show()
    }

    private fun displayPollutionDataForCity(city: String) {
        val pollutants = when (city) {
            "Delhi" -> listOf(
                Pollutant("PM2.5", "60 µg/m³", "Unhealthy"),
                Pollutant("PM10", "90 µg/m³", "Unhealthy for sensitive groups"),
                Pollutant("NO2", "40 ppb", "Moderate"),
                Pollutant("O3", "50 ppb", "Moderate")
            )
            "Mumbai" -> listOf(
                Pollutant("PM2.5", "45 µg/m³", "Moderate"),
                Pollutant("PM10", "60 µg/m³", "Moderate"),
                Pollutant("NO2", "25 ppb", "Good"),
                Pollutant("O3", "20 ppb", "Good")
            )
            "Hyderabad" -> listOf(
                Pollutant("PM2.5", "50 µg/m³", "Moderate"),
                Pollutant("PM10", "70 µg/m³", "Unhealthy for sensitive groups"),
                Pollutant("NO2", "35 ppb", "Moderate"),
                Pollutant("O3", "30 ppb", "Good"),
                Pollutant("CO", "10 ppm", "Good")
            )
            "Bengaluru" -> listOf(
                Pollutant("PM2.5", "40 µg/m³", "Good"),
                Pollutant("PM10", "50 µg/m³", "Moderate"),
                Pollutant("NO2", "20 ppb", "Good"),
                Pollutant("O3", "25 ppb", "Good")
            )
            else -> listOf(
                Pollutant("PM2.5", "56 µg/m³", "Moderate"),
                Pollutant("PM10", "78 µg/m³", "Unhealthy for sensitive groups"),
                Pollutant("NO2", "30 ppb", "Moderate"),
                Pollutant("O3", "25 ppb", "Good")
            )
        }

        pollutantAdapter = PollutantAdapter(pollutants)
        pollutionRecyclerView.adapter = pollutantAdapter
        Toast.makeText(requireContext(), "Data fetched for $city", Toast.LENGTH_SHORT).show()
    }

    private fun displayPollutionDataForCoordinates(latitude: String, longitude: String) {
        val pollutants = listOf(
            Pollutant("PM2.5", "65 µg/m³", "Unhealthy"),
            Pollutant("PM10", "80 µg/m³", "Unhealthy for sensitive groups"),
            Pollutant("NO2", "28 ppb", "Moderate"),
            Pollutant("O3", "30 ppb", "Good")
        )
        pollutantAdapter = PollutantAdapter(pollutants)
        pollutionRecyclerView.adapter = pollutantAdapter
        Toast.makeText(requireContext(), "Data fetched for coordinates: $latitude, $longitude", Toast.LENGTH_SHORT).show()
    }
}
