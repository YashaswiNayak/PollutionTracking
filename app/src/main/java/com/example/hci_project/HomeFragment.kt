package com.example.hci_project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var pollutionRecyclerView: RecyclerView
    private lateinit var pollutantAdapter: PollutantAdapter



    // List to store favorite coordinates
    private val favoriteCoordinates = mutableListOf<Pair<String, String>>()
    private val cityCoordinates = mutableMapOf(
        "Delhi" to Pair(28.7041, 77.1025),
        "Mumbai" to Pair(19.0760, 72.8777),
        "Hyderabad" to Pair(17.3850, 78.4867),
        "Bengaluru" to Pair(12.9716, 77.5946)
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val citySpinner: Spinner = view.findViewById(R.id.citySpinner)
        val fetchDataButton: Button = view.findViewById(R.id.fetchDataButton)
        val clearButton: Button = view.findViewById(R.id.clearButton)
        val refreshButton: Button = view.findViewById(R.id.refreshButton)
        pollutionRecyclerView = view.findViewById(R.id.pollutionRecyclerView)
        view?.findViewById<TextView>(R.id.cityHeading)?.text = "NOTE: Select Others from spinner to enter manual Coordinates\nData fetched will be shown here"


        pollutionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val cityList = prepareCityList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        fetchDataButton.setOnClickListener {
            val selectedCity = citySpinner.selectedItem.toString()
            if (selectedCity == "--Select City/Others--") {
                Toast.makeText(requireContext(), "Please select a city or enter coordinates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cityCoordinates.containsKey(selectedCity)) {

                val (latitude, longitude) = cityCoordinates[selectedCity]!!
                fetchAirQualityData(latitude, longitude, selectedCity)
            } else if (selectedCity == "Others") {

                showCoordinateInputDialog()
            }
        }
        refreshButton.setOnClickListener{
            val selectedCity = citySpinner.selectedItem.toString()
            if (selectedCity == "--Select City/Others--") {
                Toast.makeText(requireContext(), "Please select a city or enter coordinates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cityCoordinates.containsKey(selectedCity)) {

                val (latitude, longitude) = cityCoordinates[selectedCity]!!
                fetchAirQualityData(latitude, longitude, selectedCity)
            } else if (selectedCity == "Others") {

                showCoordinateInputDialog()
            }
        }
        clearButton.setOnClickListener {
            citySpinner.setSelection(0)
            clearPollutionData()
        }
    }

    private fun fetchAirQualityData(lat: Double, lon: Double,cityName:String) {
        view?.findViewById<TextView>(R.id.cityHeading)?.text = "City: $cityName"
        Client.instance.getAirQuality(lat, lon, "cd79c6f3c488e4ca5c0596199f5aeeb9")
            .enqueue(object : Callback<AirQualityResponse> {
                override fun onResponse(call: Call<AirQualityResponse>, response: Response<AirQualityResponse>) {
                    if (response.isSuccessful) {
                        val airQualityData = response.body()?.list?.firstOrNull()
                        airQualityData?.let { data ->

                            val pollutants = listOf(
                                Pollutant("PM2.5", "${data.components.pm2_5} µg/m³"),
                                Pollutant("PM10", "${data.components.pm10} µg/m³"),
                                Pollutant("SO2", "${data.components.so2} µg/m³"),
                                Pollutant("CO", "${data.components.co} µg/m³"),
                                Pollutant("NO2", "${data.components.no2} µg/m³"),
                                Pollutant("O3", "${data.components.o3} µg/m³")
                            ).sortedBy { pollutant ->
                                when (pollutant.name) {
                                    "PM2.5" -> 1
                                    "PM10" -> 2
                                    else -> 3
                                }
                            }

                            pollutantAdapter = PollutantAdapter(pollutants)
                            pollutionRecyclerView.adapter = pollutantAdapter
                            Toast.makeText(requireContext(), "Data fetched for coordinates: $lat, $lon", Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch data: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Failed to load data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun prepareCityList(): List<String> {
        val cityList = mutableListOf("--Select City/Others--")
        cityList.addAll(cityCoordinates.keys)
        cityList.add("Others") // Ensure "Others" is always at the bottom
        return cityList
    }


    private fun clearPollutionData() {
        pollutantAdapter = PollutantAdapter(emptyList())
        pollutionRecyclerView.adapter = pollutantAdapter

        view?.findViewById<TextView>(R.id.cityHeading)?.text = "NOTE: Select Others from spinner to enter manual Coordinates\nData fetched will be shown here"
        Toast.makeText(requireContext(), "Data cleared", Toast.LENGTH_SHORT).show()
    }

    private fun showCoordinateInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_coordinates, null)
        val latitudeInput: EditText = dialogView.findViewById(R.id.latitudeInput)
        val longitudeInput: EditText = dialogView.findViewById(R.id.longitudeInput)
        val addToFavoritesCheckbox: CheckBox = dialogView.findViewById(R.id.favoriteCheckBox)

        AlertDialog.Builder(requireContext())
            .setTitle("Enter Coordinates")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val latitude = latitudeInput.text.toString()
                val longitude = longitudeInput.text.toString()

                if (latitude.isNotBlank() && longitude.isNotBlank()) {
                    if (addToFavoritesCheckbox.isChecked) {
                        addFavoriteCoordinate(latitude, longitude)
                    }
                    displayPollutionDataForCoordinates(latitude, longitude)
                } else {
                    Toast.makeText(requireContext(), "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun addFavoriteCoordinate(lat: String, lon: String) {
        fetchCityNameFromCoordinates(lat, lon) { cityName ->
            favoriteCoordinates.add(Pair(lat, lon))
            cityCoordinates[cityName] = Pair(lat.toDouble(), lon.toDouble())

            // Refresh the spinner with the updated list
            val citySpinner: Spinner = view?.findViewById(R.id.citySpinner)!!
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prepareCityList())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            citySpinner.adapter = adapter
            Toast.makeText(requireContext(), "Coordinate added to favorites", Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchCityNameFromCoordinates(lat: String, lon: String, callback: (String) -> Unit) {
        val latDouble = lat.toDoubleOrNull()
        val lonDouble = lon.toDoubleOrNull()

        if (latDouble == null || lonDouble == null) {
            callback("Invalid Coordinates")
            return
        }

        GeocodingClient.geocodingService.getCityNameFromCoordinates(
            lat = latDouble,
            lon = lonDouble,
            apiKey = "cd79c6f3c488e4ca5c0596199f5aeeb9"
        ).enqueue(object : Callback<List<GeocodeResponse>> {
            override fun onResponse(call: Call<List<GeocodeResponse>>, response: Response<List<GeocodeResponse>>) {
                if (response.isSuccessful) {
                    val cityName = response.body()?.firstOrNull()?.name ?: "Unknown Location ($lat, $lon)"
                    view?.findViewById<TextView>(R.id.cityHeading)?.text = "$cityName"
                    Log.d("Geocoding", "Response: $cityName")
                    callback(cityName)
                } else {
                    Log.e("Geocoding", "Error: ${response.message()}")
                    callback("Unknown Location ($lat, $lon)") // Fallback
                }
            }

            override fun onFailure(call: Call<List<GeocodeResponse>>, t: Throwable) {
                Log.e("Geocoding", "Failed: ${t.message}")
                callback("Unknown Location ($lat, $lon)") // Fallback
            }
        })
    }




    private fun displayPollutionDataForCoordinates(lat: String, lon: String) {
        fetchAirQualityData(lat.toDouble(), lon.toDouble(),"$lat, $lon")
    }

}





