package com.example.hci_project

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)  // Ensure you set the correct layout for this activity


        // Optionally, set a title or customize the toolbar furth

    }

    // Handle back button behavior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // This will navigate the user back to the previous screen
        return true
    }
}
