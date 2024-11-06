package com.example.hci_project

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PollutantAdapter(private val pollutants: List<Pollutant>) : RecyclerView.Adapter<PollutantAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.pollutantName)
        val levelTextView: TextView = view.findViewById(R.id.pollutantLevel)
        val advisoryTextView: TextView = view.findViewById(R.id.pollutantAdvisory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pollutant_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pollutant = pollutants[position]

        holder.nameTextView.text = pollutant.name
        holder.levelTextView.text = pollutant.level

        // Convert pollutant.level from String to Float
        val pollutantValue = pollutant.level.split(" ")[0] // Extract the numeric value
        val pollutantUnit = pollutant.level.split(" ").getOrElse(1) { "µg/m³" } // Default to µg/m³ if missing

        // Convert the value to Float
        val level = pollutantValue.toFloat()  // Default to 0 if conversion fails

        // Determine AQI level and advisory based on pollutant concentration
        val (advisory, color) = when (pollutant.name) {
            "SO2" -> getAQIAdvisoryAndColor(level, listOf(20, 80, 250, 350))
            "NO2" -> getAQIAdvisoryAndColor(level, listOf(40, 70, 150, 200))
            "PM10" -> getAQIAdvisoryAndColor(level, listOf(20, 50, 100, 200))
            "PM2.5" -> getAQIAdvisoryAndColor(level, listOf(10, 25, 50, 75))
            "O3" -> getAQIAdvisoryAndColor(level, listOf(60, 100, 140, 180))
            "CO" -> getAQIAdvisoryAndColor(level, listOf(4400, 9400, 12400, 15400))
            else -> "Unknown" to Color.BLACK
        }

        holder.advisoryTextView.text = advisory
        holder.advisoryTextView.setTextColor(color)
    }

    override fun getItemCount(): Int = pollutants.size

    // Helper function to determine AQI level and color
    private fun getAQIAdvisoryAndColor(value: Float, thresholds: List<Int>): Pair<String, Int> {
        return when {
            value <= thresholds[0] -> "Good" to Color.GREEN
            value <= thresholds[1] -> "Fair" to Color.rgb(204, 204, 0)
            value <= thresholds[2] -> "Moderate" to Color.rgb(255, 165, 0)
            value <= thresholds[3] -> "Poor" to Color.RED
            else -> "Very Poor" to Color.rgb(128, 0, 0)
        }
    }
}
