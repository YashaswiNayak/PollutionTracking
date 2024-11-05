package com.example.hci_project

import android.content.Context
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
        holder.advisoryTextView.text = pollutant.advisory

        // Set color based on advisory level
        val levelValue = pollutant.level.split(" ")[0].toIntOrNull() // Extracting numerical part
        if (levelValue != null) {
            holder.advisoryTextView.setTextColor(
                when {
                    levelValue <= 40 -> {
                        // Good - Green
                        holder.advisoryTextView.resources.getColor(android.R.color.holo_green_light)
                    }
                    levelValue in 41..80 -> {
                        // Moderate - Yellow
                        holder.advisoryTextView.resources.getColor(android.R.color.holo_orange_light)
                    }
                    else -> {
                        // Unhealthy - Red
                        holder.advisoryTextView.resources.getColor(android.R.color.holo_red_light)
                    }
                }
            )
        } else {
            // Fallback color if level cannot be parsed
            holder.advisoryTextView.setTextColor(holder.advisoryTextView.resources.getColor(android.R.color.black))
        }
    }

    override fun getItemCount(): Int = pollutants.size
}
